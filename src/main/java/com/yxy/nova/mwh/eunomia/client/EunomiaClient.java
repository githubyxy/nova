package com.yxy.nova.mwh.eunomia.client;

import com.yxy.nova.mwh.eunomia.client.listener.EunomiaListener;
import com.yxy.nova.mwh.eunomia.client.listener.EunomiaListenerCycle;

import java.util.List;

/**
 * Created by toruneko on 2016/12/7.
 */
public interface EunomiaClient {

    void removeListener(String filter);

    boolean hasListener(String filter);

    void addListener(String filter, EunomiaListener listener);

    EunomiaListenerCycle getListener(String filter);

    List<EunomiaListenerCycle> getListeners();
}
