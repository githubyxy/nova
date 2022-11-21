package com.yxy.nova.mwh.eunomia.client.listener;

import com.yxy.nova.mwh.eunomia.client.exception.EunomiaClientException;

/**
 * Created by toruneko on 2016/12/12.
 */
public interface EunomiaListenerCycle {

    String getName();

    boolean isStart() throws EunomiaClientException;

    void start() throws EunomiaClientException;

    void stop() throws EunomiaClientException;

}
