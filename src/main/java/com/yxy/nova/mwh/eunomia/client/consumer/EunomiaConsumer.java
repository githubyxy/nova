package com.yxy.nova.mwh.eunomia.client.consumer;

/**
 * Created by toruneko on 2016/12/6.
 */
public interface EunomiaConsumer {

    String getName();

    boolean onMessage(byte[] message);

}
