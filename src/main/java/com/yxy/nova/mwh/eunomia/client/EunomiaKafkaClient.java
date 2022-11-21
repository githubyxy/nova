package com.yxy.nova.mwh.eunomia.client;

import com.yxy.nova.mwh.eunomia.client.consumer.kafka.EunomiaKafkaConsumer;
import com.yxy.nova.mwh.eunomia.client.consumer.mock.EunomiaMockConsumer;
import com.yxy.nova.mwh.eunomia.client.listener.EunomiaListener;
import com.yxy.nova.mwh.eunomia.client.listener.EunomiaListenerConfig;
import com.yxy.nova.mwh.eunomia.client.listener.EunomiaListenerCycle;
import com.yxy.nova.mwh.kafka.object.ClusterConfig;
import com.yxy.nova.mwh.kafka.object.ConsumerConfig;
import com.yxy.nova.mwh.kafka.object.SubscriberConfig;
import com.yxy.nova.mwh.kafka.object.TopicConfig;
import com.yxy.nova.mwh.kafka.util.IConfigCenter;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;

/**
 * Created by toruneko on 2016/12/7.
 */
public class EunomiaKafkaClient extends AbstractEunomiaClient implements Closeable {

    private String brokers;

    private String groupId;

    private String prefix;

    private boolean disabled = false;

    /**
     * 启动的消费线程数
     */
    private int threadCount = 1;

    @Override
    public void close() throws IOException {
        super.close();
    }

    protected IConfigCenter configCenter(final String topic, final EunomiaListener listener) {
        return new IConfigCenter() {

            private static final String clusterName = "eunomia";

            @Override
            public boolean isNaive() {
                // 是否允许默认配置
                return false;
            }

            @Override
            public String queryInfluxdb() {
                return null;
            }

            @Override
            public ClusterConfig queryCluster(String cluster) {
                ClusterConfig cc = new ClusterConfig();
                cc.setBrokers(getBrokers());
                cc.setName(clusterName);
                return cc;
            }

            @Override
            public TopicConfig queryTopic(String topic) {
                TopicConfig tc = new TopicConfig();
                tc.setClusterName(clusterName);
                tc.setTopic(topic);
                return tc;
            }

            @Override
            public ConsumerConfig queryConsumer(String name) {
                ConsumerConfig   consumerConfig   = new ConsumerConfig();
                SubscriberConfig subscriberConfig = new SubscriberConfig();
                if (listener instanceof EunomiaListenerConfig) {
                    consumerConfig.setName(((EunomiaListenerConfig) listener).groupId(getGroupId()));
                    subscriberConfig.put("cc", ((EunomiaListenerConfig) listener).threadCount(getThreadCount()));
                } else {
                    consumerConfig.setName(getGroupId());
                    subscriberConfig.put("cc", getThreadCount());
                }
                subscriberConfig.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 200);
                consumerConfig.setSubscribed(Collections.singletonMap(topic, subscriberConfig));

                return consumerConfig;
            }

            @Override
            public void close() throws IOException {

            }
        };
    }

    @Override
    public EunomiaListenerCycle createListenerCycle(String filter, EunomiaListener listener) {
        String topic = generateTopic(filter);
        if (isDisabled()) {
            return new EunomiaMockConsumer(topic);
        }
        return new EunomiaKafkaConsumer(topic, listener, configCenter(topic, listener));
    }

    protected String generateTopic(String filter) {
        if (getPrefix() != null && !"".equals(getPrefix())) {
            filter = getPrefix() + "." + filter;
        }
        return filter.replaceAll("\\.", "_");
    }

    public String getBrokers() {
        return brokers;
    }

    public void setBrokers(String brokers) {
        this.brokers = brokers;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        if (threadCount < 1) {
            threadCount = 1;
        }
        this.threadCount = threadCount;
    }
}
