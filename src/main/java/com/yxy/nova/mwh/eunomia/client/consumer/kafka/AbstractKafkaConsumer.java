package com.yxy.nova.mwh.eunomia.client.consumer.kafka;

import com.yxy.nova.mwh.eunomia.client.exception.EunomiaClientException;
import com.yxy.nova.mwh.eunomia.client.listener.EunomiaListenerCycle;
import com.yxy.nova.mwh.kafka.consumer.IConsumer;
import com.yxy.nova.mwh.kafka.consumer.Mode;
import com.yxy.nova.mwh.kafka.consumer.StandardPoller;
import com.yxy.nova.mwh.kafka.object.ConsumerConfig;
import com.yxy.nova.mwh.kafka.util.IConfigCenter;

import java.io.IOException;
import java.util.Collections;


/**
 * Created by toruneko on 2016/12/7.
 */
public abstract class AbstractKafkaConsumer extends StandardPoller implements EunomiaListenerCycle {

    private static final String EMPTY = "";

    private String topic;

    public AbstractKafkaConsumer(String topic, IConfigCenter configCenter) {
        this.topic = topic;

        ConsumerConfig consumerConfig = configCenter.queryConsumer(EMPTY);
        setConsumerName(consumerConfig.getName());
        setConfigCenter(configCenter);
        setMode(Mode.STANDARD.name());
        setBizConsumers(Collections.singletonMap(topic, getConsumer()));

        running.set(false);
    }

    protected abstract IConsumer getConsumer();

    @Override
    public String getName() {
        return topic;
    }

    @Override
    public boolean isStart() {
        return running.get();
    }

    @Override
    public void start() throws EunomiaClientException {
        try {
            if (running.compareAndSet(false, true)) {
                init();
            }
        } catch (Exception e) {
            throw new EunomiaClientException(e);
        }
    }

    @Override
    public void stop() throws EunomiaClientException {
        try {
            if (running.compareAndSet(true, false)) {
                close();
            }
        } catch (IOException e) {
            throw new EunomiaClientException(e);
        }
    }
}
