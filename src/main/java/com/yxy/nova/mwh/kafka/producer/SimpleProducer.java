package com.yxy.nova.mwh.kafka.producer;

import com.yxy.nova.mwh.kafka.object.ClusterConfig;
import com.yxy.nova.mwh.kafka.object.ProducerException;
import com.yxy.nova.mwh.kafka.object.QueueJob;
import com.yxy.nova.mwh.kafka.object.TopicConfig;
import com.yxy.nova.mwh.kafka.util.ErrorHelper;
import com.yxy.nova.mwh.kafka.util.IConfigCenter;
import com.yxy.nova.mwh.kafka.util.PerfReporter;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 标准消息发送器
 *
 * 若配置了多个topic,请咨询管理员并确保这些topic位于同一个集群下,否则会报错
 *
 * Created by xiazhen on 18/6/27.
 */
public class SimpleProducer implements Closeable, IProducer {

    protected static final Logger log = LoggerFactory.getLogger("module-kafka");

    private boolean running = true;
    private boolean persistOnError = false;
    private String persistenceFile = "";
    private DiskQueue diskQueue;

    private List<String> topics;
    private TopicConfig defaultTopicConfig;
    private ClusterConfig clusterConfig;
    private IConfigCenter configCenter;
    private Producer<String, byte[]> producer;
    private Map<String, String> overrides;

    public void init() throws IOException {
        Preconditions.checkArgument(getTopics() != null && getTopics().size() > 0, "topic list is empty");
        // 获取zk下 /kafka-mq/{default.business.unit}/topic/{topic} 下的TopicConfig
        this.defaultTopicConfig = configCenter.queryTopic(getTopics().get(0));
        Preconditions.checkArgument(defaultTopicConfig != null, "topic is not registered");

        for (final String topic : getTopics()) {
            final TopicConfig tc = configCenter.queryTopic(topic);
            Preconditions.checkArgument(tc != null, "topic not registered:" + topic);
            Preconditions.checkArgument(
                    tc.getClusterName().equalsIgnoreCase(this.defaultTopicConfig.getClusterName()),
                    "topic doesn't belong to the same cluster:" + topic
            );
        }
        // 获取zk下 /kafka-mq/{default.business.unit}/cluster/{clusterName} 下的ClusterConfig
        this.clusterConfig = configCenter.queryCluster(defaultTopicConfig.getClusterName());
        build();
        PerfReporter.init(configCenter.queryInfluxdb(), "p");
        if (persistOnError) {
            diskQueue = new DiskQueue(this, persistenceFile);
            diskQueue.start();
        }
        log.warn("SimpleProducer initiated successfully");
    }

    private void build() {
        final Properties props = new Properties();
        props.put("bootstrap.servers", clusterConfig.getBrokers());
        props.put("acks", String.valueOf(defaultTopicConfig.getAcks()));
        props.put("retries", defaultTopicConfig.getRetries());
        props.put("batch.size", 16384);
        props.put("max.request.size", defaultTopicConfig.getMaxSize());
        props.put("linger.ms", defaultTopicConfig.getLinger());
        props.put("compression.type", defaultTopicConfig.getCompressor());
        props.put("buffer.memory", defaultTopicConfig.getBufferSize());
        props.put("metric.reporters", "com.yxy.nova.mwh.kafka.util.DefaultMetricsReporter");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        if (overrides != null) {
            props.putAll(overrides);
        }
        this.producer = new KafkaProducer<>(props);
    }

    @Override
    public void close() throws IOException {
        if (!running) return;//already closed
        if (diskQueue != null) {
            diskQueue.close();
        }
        this.running = false;
        if (this.producer != null) {
            this.producer.flush();
            log.warn("producer closed for {}", defaultTopicConfig.getTopic());
            this.producer.close();
        }
        PerfReporter.close();
    }

    public TopicConfig getDefaultTopicConfig() {
        return defaultTopicConfig;
    }

    public IConfigCenter getConfigCenter() {
        return configCenter;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public void setConfigCenter(IConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    public void setPersistOnError(boolean persistOnError) {
        this.persistOnError = persistOnError;
    }

    public void setPersistenceFile(String persistenceFile) {
        this.persistenceFile = persistenceFile;
    }

    public void setOverrides(Map<String, String> overrides) {
        this.overrides = overrides;
    }

    @Override
    public void produce(String topic, String messageKey, byte[] message) throws ProducerException {
        final QueueJob job = new QueueJob(topic, messageKey, message, System.currentTimeMillis());
        internalProduce(job, null, null);
    }

    @Override
    public void produce(String topic, String messageKey, byte[] message, Callback callback) throws ProducerException {
        checkArg(callback != null, "invalid callback");
        final QueueJob job = new QueueJob(topic, messageKey, message, System.currentTimeMillis());
        internalProduce(job, null, callback);
    }

    @Override
    public boolean syncProduce(String topic, String messageKey, byte[] message) throws ProducerException {
        final QueueJob job = new QueueJob(topic, messageKey, message, System.currentTimeMillis());
        final Future<RecordMetadata> future = internalProduce(job, null, null);
        try {
            if (future == null) return true;
            final RecordMetadata meta = future.get();//blocking and may throw exception !!!
            return future.isDone() && meta.hasOffset();
        } catch (InterruptedException | ExecutionException e) {
            if (e.getCause() != null && e.getCause() instanceof Exception) {
                onError(job, null, (Exception) e.getCause());
            } else {
                onError(job, null, e);
            }
            return false;
        }
    }

    private void onError(final QueueJob job, final Callback callback, final Exception e) throws ProducerException {
        log.error("produce failed:" + job.getMessageKey(), e);
        if (callback != null) {
            //if callback exists then return without persistence
            callback.onCompletion(null, e);
            return;
        }
        if (ErrorHelper.isRecoverable(e)) {
            if (persistOnError) {
                int size = diskQueue.append(job);
                log.error("recoverable exception for msg:{},current disk queue size:{}", job.getMessageKey(), size);
                return;
            }
            throw new ProducerException(e);
        } else {
            log.error("persistOnError disabled or exception not recoverable:{}", job.getMessageKey());
            throw ProducerException.fatal(e);
        }
    }

    protected Future<RecordMetadata> internalProduce (final QueueJob job, final Integer partition, final Callback cb) throws ProducerException {
        checkArg(job.getMessage() != null && job.getMessage().length < defaultTopicConfig.getMaxSize(),
                "message too long while max length in byte is " + defaultTopicConfig.getMaxSize()
        );
        checkArg(running, "failed to send because producer is closed");
        checkArg(topics.contains(job.getTopic()), "topic is not configured in the list");
        checkArg(StringUtils.isNotEmpty(job.getMessageKey()), "message key is empty");

        final ProducerRecord<String, byte[]> rec = new ProducerRecord<>(
                job.getTopic(), partition,  job.getMessageKey(), job.getMessage()
        );
        try {
            return this.producer.send(rec, cb);
        } catch (Exception e) {
            onError(job, cb, e);
            return null;
        }
    }

    protected void checkArg(boolean expr, String msg) throws ProducerException {
        if (!expr) throw ProducerException.invalidParam(msg);
    }
}
