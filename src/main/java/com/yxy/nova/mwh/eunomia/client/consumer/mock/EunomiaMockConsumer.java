package com.yxy.nova.mwh.eunomia.client.consumer.mock;

import com.yxy.nova.mwh.eunomia.client.exception.EunomiaClientException;
import com.yxy.nova.mwh.eunomia.client.listener.EunomiaListenerCycle;

import java.util.concurrent.atomic.AtomicBoolean;

public class EunomiaMockConsumer implements EunomiaListenerCycle {

    private String topic;

    private AtomicBoolean started = new AtomicBoolean(false);

    public EunomiaMockConsumer(String topic) {
        this.topic = topic;
    }

    @Override
    public String getName() {
        return topic;
    }

    @Override
    public boolean isStart() throws EunomiaClientException {
        return started.get();
    }

    @Override
    public void start() throws EunomiaClientException {
        started.compareAndSet(false, true);
    }

    @Override
    public void stop() throws EunomiaClientException {
        started.compareAndSet(true, false);
    }
}
