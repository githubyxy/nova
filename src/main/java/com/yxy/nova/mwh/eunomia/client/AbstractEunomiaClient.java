package com.yxy.nova.mwh.eunomia.client;

import com.yxy.nova.mwh.eunomia.client.exception.EunomiaClientException;
import com.yxy.nova.mwh.eunomia.client.listener.EunomiaListener;
import com.yxy.nova.mwh.eunomia.client.listener.EunomiaListenerCycle;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by toruneko on 2016/12/6.
 */
public abstract class AbstractEunomiaClient implements EunomiaClient, Closeable {

    private Map<String, EunomiaListenerCycle> listenerCycleMap = new ConcurrentHashMap<>();

    @Override
    public void removeListener(String filter) {
        if (!hasListener(filter)) {
            throw new EunomiaClientException("listener cycle " + filter + " not exists");
        }

        EunomiaListenerCycle listenerCycle = listenerCycleMap.remove(filter);
        if (listenerCycle != null && listenerCycle.isStart()) {
            listenerCycle.stop();
        }
    }

    @Override
    public boolean hasListener(String filter) {
        return listenerCycleMap.containsKey(filter);
    }

    @Override
    public void addListener(String filter, EunomiaListener listener) {
        if (hasListener(filter)) {
            throw new EunomiaClientException("listener cycle  " + filter + " has already exists");
        }

        EunomiaListenerCycle listenerCycle = createListenerCycle(filter, listener);
        if (!listenerCycle.isStart()) {
            listenerCycle.start();
        }
        listenerCycleMap.put(filter, listenerCycle);
    }

    @Override
    public EunomiaListenerCycle getListener(String filter) {
        return listenerCycleMap.get(filter);
    }

    @Override
    public List<EunomiaListenerCycle> getListeners() {
        return new ArrayList<>(listenerCycleMap.values());
    }

    @Override
    public void close() throws IOException {
        for (EunomiaListenerCycle listenerCycle : listenerCycleMap.values()) {
            if (listenerCycle.isStart()) {
                listenerCycle.stop();
            }
        }
        listenerCycleMap.clear();
    }

    public abstract EunomiaListenerCycle createListenerCycle(String filter, EunomiaListener listener);
}
