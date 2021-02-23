package com.yxy.nova.mwh.kafka.util;

import com.yxy.nova.mwh.kafka.consumer.ConcurrentPoller;
import com.yxy.nova.mwh.kafka.consumer.IConsumer;
import com.yxy.nova.mwh.kafka.consumer.StandardPoller;
import com.yxy.nova.mwh.kafka.object.ComplexTopic;
import com.yxy.nova.mwh.kafka.object.RetryLaterException;
import com.yxy.nova.mwh.kafka.producer.IProducer;
import com.yxy.nova.mwh.kafka.producer.SimpleProducer;
import com.google.common.collect.Sets;
import com.yammer.metrics.core.Counter;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricsRegistry;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.metrics.KafkaMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 一个自发自收的压测程序 仅在线下开发环境运行
 *
 * Created by xiazhen on 18/6/27.
 */
public class AppTest {

    protected static final Logger log = LoggerFactory.getLogger("module-kafka");
    protected static final MetricsRegistry metrcis = new MetricsRegistry();

    private IConfigCenter configCenter;
    private IProducer producer;
    private StandardPoller poller;
    private Timer timer = new Timer();

    private static final Random random = new Random();
    private static final Counter traffic = metrcis.newCounter(new MetricName("", "", "traffic"));
    private static final Counter received =  metrcis.newCounter(new MetricName("", "", "received"));

    private long lastRecvTime = System.currentTimeMillis();

    private final ComplexTopic complexTopic = ComplexTopic.create(MockConfigCenter.COMPLEX_TOPIC);
    private final Set<String> sentMsg = Sets.newConcurrentHashSet();
    private final Set<String> recvMsg = Sets.newConcurrentHashSet();
    private final AtomicInteger retryExceptionMsg = new AtomicInteger();


    public static void main(String[] argv) throws Exception {
        log.warn("apptest started!!!");
        long send = Long.parseLong(System.getProperty("send", "10"));
        System.out.printf("will send %s msg\n", send);

        AppTest app = new AppTest();
        app.setup();
        if (send > 0) {
            app.runProducer(send);
        }
        Thread.sleep(10 * 1000);
        while (true) {
            if (System.currentTimeMillis() - app.lastRecvTime > 600 * 1000) {
                app.configCenter.close();
                app.poller.close();
                app.timer.cancel();
                System.out.println("recv contains all: " + app.recvMsg.containsAll(app.sentMsg));
                System.out.printf("no further message is received! recvMsg:%d, sentMsg:%d\n", app.recvMsg.size(), app.sentMsg.size());
                System.out.println("retryLaterException messages count:" + app.retryExceptionMsg.get());
                break;
            }
        }
    }

    private static String map2String(Map<String, String> m) {
        final StringBuilder sb = new StringBuilder();
        if (m != null) {
            for (Map.Entry<String, String> e : m.entrySet()) {
                sb.append(e.getKey()).append('=').append(e.getValue()).append(',');
            }
        }
        return sb.toString();
    }


    /**
     * only allowed in test env
     */
    public void enableConsoleReporter() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Map<org.apache.kafka.common.MetricName, KafkaMetric> data = DefaultMetricsReporter.getData();
                System.out.printf("metrics:\n");
                for (Map.Entry<org.apache.kafka.common.MetricName, KafkaMetric> e : data.entrySet()) {
                    System.out.printf("%s:%s[%s]=%.3f\n",
                            e.getKey().group(),
                            e.getKey().name(),
                            map2String(e.getKey().tags()),
                            e.getValue().metricValue());
                }
                System.out.printf("traffic=%d, received=%d\n",
                        metrcis.newCounter(new MetricName("", "", "traffic")).count(),
                        metrcis.newCounter(new MetricName("", "", "received")).count()
                );

            }
        }, 1000, 60 * 1000);
    }

    public void setup() throws Exception {
        enableConsoleReporter();
        final ZKConfigCenter zcc = new ZKConfigCenter();
        zcc.setZkserver("127.0.0.1:2181");
        zcc.setBusinessUnit("dev");
        zcc.init();
        this.configCenter = zcc;

        final SimpleProducer sp = new SimpleProducer();
        sp.setConfigCenter(configCenter);
        sp.setTopics(complexTopic.getParts());
        sp.init();
        sp.setPersistOnError(true);
        sp.setPersistenceFile("/tmp/module-kafka-apptest.bin");
        producer = sp;

        final String type = System.getProperty("poller", "cc");
        if (type.equalsIgnoreCase("cc")) {
            poller = new ConcurrentPoller();
        } else {
            poller = new StandardPoller();
        }
        poller.setConfigCenter(configCenter);
        poller.setConsumerName("perf-test-consumer2");
        poller.setBizConsumers(Collections.<String, IConsumer>singletonMap(complexTopic.get(), new MyConsumer()));
        poller.init();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    producer.close();
                    poller.close();
                    log.warn("final received:{}", received.count());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("setup completed!");
    }

    public void runProducer(final long send) throws Exception {
        final long start = System.currentTimeMillis();
        System.out.println("runProducer...");
        final String msg1 = UUID.randomUUID().toString();
        producer.syncProduce(MockConfigCenter.TOPIC, msg1, UUID.randomUUID().toString().getBytes("UTF-8"));
        sentMsg.add(msg1);

        for (long i = 1; i < send; i++) {
            final byte[] data = new byte[random.nextInt(10 * 900)+3000];
            for (int n = 0; n < data.length; n++) {
                data[n] = (byte) (random.nextInt(60) + 48);
            }
            final String topic = complexTopic.getParts().get((random.nextInt(2)));
            final String msgKey = UUID.randomUUID().toString();
            producer.produce(topic, msgKey, data, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception == null) {
                        sentMsg.add(msgKey);
                    } else {
                        log.error("failed to sent msg:" + msgKey);
                    }
                }
            });
        }
        producer.close();
        System.out.printf("runProducer ended within %d seconds\n", (System.currentTimeMillis() - start) / 1000);
        System.out.printf("%d messages have been sent\n", sentMsg.size());
    }

    public class MyConsumer implements IConsumer {
        private AtomicBoolean threw = new AtomicBoolean(false);

        @Override
        public void doConsume(List<ConsumerRecord<String, byte[]>> messages) {
            lastRecvTime = System.currentTimeMillis();
            final ConsumerRecord meta = messages.get(0);
            log.warn("called once >> {}, topic:{}, pt:{}", messages.size(), meta.topic(), meta.partition());
            final int cnt = messages.size();
            received.inc(cnt);
            try {
                if (meta.partition() == 2 &&
                        meta.topic().equalsIgnoreCase("perf-test") &&
                        cnt > 1 &&
                        threw.compareAndSet(false, true)) {
                    throw new Exception();
                }
                Thread.sleep(random.nextInt(5) + 1);
            } catch (Exception e) {
                log.warn("RetryLaterException:" + cnt);
                retryExceptionMsg.addAndGet(cnt);
                throw new RetryLaterException();
            }

            for (final ConsumerRecord<String, byte[]> rec : messages) {
                traffic.inc(rec.value().length);
                //recvMsg.add(rec.key());
            }
        }
    }
}
