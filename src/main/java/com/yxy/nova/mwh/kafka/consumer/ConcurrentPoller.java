package com.yxy.nova.mwh.kafka.consumer;

import com.yxy.nova.mwh.kafka.object.ComplexTopic;
import com.yxy.nova.mwh.kafka.object.RetryLaterException;
import com.google.common.collect.Maps;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 *
 * 多线程消费者
 *
 * 多个线程共享一个KafkaConsumer实例
 *
 * 总线程数=TOPIC数量*被分配的Partition数量+1
 *
 * Created by xiazhen on 18/6/28.
 */
public class ConcurrentPoller extends StandardPoller {

    private final Map<TopicPartition, ExecutorService> executor = Maps.newConcurrentMap();

    @Override
    protected int concurrentConsumer(final ComplexTopic ct) {
        //only one KafkaConsumer in order to reduce connections
        return 1;
    }

    private ExecutorService newSingleCachedThreadPool() {
        final ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1,
                60 * 1000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        //when rebalanced, some TopicPartition might have been moved to another node
        //so remaining ExecutorService thread should be terminated in order to reduce CPU utilization.
        pool.allowCoreThreadTimeOut(true);
        return pool;
    }

    @Override
    protected void kickOff(final String name, final KafkaConsumer<String, byte[]> consumer) {
        final Runnable poller = new Runnable() {
            @Override
            public void run() {
                try {
                    //only one thread is allowed to enter !!!
                    while (running.get()) {
                        final ConsumerRecords<String, byte[]> records = consumer.poll(consumerConfig.getPollInterval());
                        if (records == null || records.isEmpty()) {
                            continue;
                        }

                        final Map<TopicPartition, OffsetAndMetadata> offsets = Maps.newHashMap();
                        final CountDownLatch latch = new CountDownLatch(records.partitions().size());
                        for (final TopicPartition partition : records.partitions()) {
                            final List<ConsumerRecord<String, byte[]>> ptRecords = records.records(partition);
                            final long newOffset = ptRecords.get(ptRecords.size() - 1).offset() + 1;
                            final IConsumer bizConsumer = bizConsumerMap.get(partition.topic());
                            if (!executor.containsKey(partition)) {
                                executor.put(partition, newSingleCachedThreadPool());
                            }
                            final ExecutorService es = executor.get(partition);
                            es.submit(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        bizConsumer.doConsume(ptRecords);
                                        offsets.put(partition, new OffsetAndMetadata(newOffset));
                                    } catch (RetryLaterException re) {
                                        consumer.seek(partition, ptRecords.get(0).offset());
                                    } catch (Throwable e) {
                                        log.error(ERR_UNEXPECTED_EXCEPTION, e);
                                    } finally {
                                        latch.countDown();
                                    }
                                }
                            });
                        }
                        latch.await();
                        commit(consumer, offsets);
                    }
                } catch (WakeupException e) {
                    log.warn("ConcurrentPoller is being closing");
                } catch (Exception e) {
                    log.error("ConcurrentPoller encountered fatal error and shutting down:", e);
                } finally {
                    log.warn("ConcurrentPoller closing:" + name);
                    consumer.close();
                }
            }
        };

        new Thread(poller, THREAD_NAME_PFX + "CC-" + name).start();
        log.warn("ConcurrentPoller started:{}", name);
    }

    @Override
    public void close() throws IOException {
        super.close();
        for (final ExecutorService es : executor.values()) {
            try {
                es.shutdown();
                es.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                log.error("failed to close executors", e);
            }
        }
    }
}

