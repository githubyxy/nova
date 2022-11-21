package com.yxy.nova.mwh.eunomia.recipes.bulk;

import com.yxy.nova.mwh.eunomia.client.EunomiaKafkaClient;
import com.yxy.nova.mwh.eunomia.client.consumer.mock.EunomiaMockConsumer;
import com.yxy.nova.mwh.eunomia.client.exception.EunomiaClientException;
import com.yxy.nova.mwh.eunomia.client.listener.EunomiaListener;
import com.yxy.nova.mwh.eunomia.client.listener.EunomiaListenerCycle;
import com.yxy.nova.mwh.kafka.object.ClusterConfig;
import com.yxy.nova.mwh.kafka.object.ConsumerConfig;
import com.yxy.nova.mwh.kafka.object.SubscriberConfig;
import com.yxy.nova.mwh.kafka.object.TopicConfig;
import com.yxy.nova.mwh.kafka.util.IConfigCenter;

import java.io.IOException;

/**
 * Created by toruneko on 2017/4/18.
 */
public class EunomiaKafkaBulkClient extends EunomiaKafkaClient {

    private int bulkSize = 200;

    private int bulkInterval = 1000;

    @Override
    public EunomiaListenerCycle createListenerCycle(String filter, EunomiaListener listener) {
        if (!(listener instanceof EunomiaBulkListener)) {
            throw new EunomiaClientException("Not a eunomia bulk listener");
        }

        String topic = generateTopic(filter);
        if (isDisabled()) {
            return new EunomiaMockConsumer(topic);
        }

        return new EunomiaKafkaBulkConsumer(topic, (EunomiaBulkListener) listener, configCenter(topic, listener));
    }

    @Override
    protected IConfigCenter configCenter(final String topic, final EunomiaListener listener) {
        final IConfigCenter configCenter = super.configCenter(topic, listener);

        return new IConfigCenter() {

            @Override
            public boolean isNaive() {
                return configCenter.isNaive();
            }

            @Override
            public String queryInfluxdb() {
                return configCenter.queryInfluxdb();
            }

            @Override
            public ClusterConfig queryCluster(String cluster) {
                return configCenter.queryCluster(cluster);
            }

            @Override
            public TopicConfig queryTopic(String topic) {
                return configCenter.queryTopic(topic);
            }

            @Override
            public ConsumerConfig queryConsumer(String name) {
                ConsumerConfig   consumerConfig   = configCenter.queryConsumer(name);
                SubscriberConfig subscriberConfig = consumerConfig.getOne(topic, isNaive());

                if (listener instanceof EunomiaBulkListenerConfig) {
                    EunomiaBulkListenerConfig listenerConfig = (EunomiaBulkListenerConfig) listener;
                    consumerConfig.setPollInterval(listenerConfig.bulkInterval(getBulkInterval()));
                    subscriberConfig.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG,
                                         listenerConfig.bulkSize(getBulkSize()));
                } else {
                    consumerConfig.setPollInterval(getBulkInterval());
                    subscriberConfig.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG,
                                         getBulkSize());
                }

                return consumerConfig;
            }

            @Override
            public void close() throws IOException {
                configCenter.close();
            }
        };
    }

    public int getBulkSize() {
        return bulkSize;
    }

    public int getBulkInterval() {
        return bulkInterval;
    }

    public void setBulkSize(int bulkSize) {
        this.bulkSize = bulkSize;
    }

    public void setBulkInterval(int bulkInterval) {
        this.bulkInterval = bulkInterval;
    }
}
