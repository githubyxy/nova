package com.yxy.nova.mwh.eunomia.recipes;

import com.yxy.nova.mwh.eunomia.client.EunomiaClient;
import com.yxy.nova.mwh.eunomia.client.listener.EunomiaListener;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by toruneko on 2017/4/18.
 */
public class EunomiaListenerRegistry implements InitializingBean {

    private EunomiaClient client;

    private Map<String, EunomiaListener> listeners;

    public void setClient(EunomiaClient client) {
        this.client = client;
    }

    public void setListeners(Map<String, EunomiaListener> listeners) {
        this.listeners = listeners;
    }

    private void initRegister(EunomiaClient client, String filter, EunomiaListener listener) {
        Map<String, EunomiaListener> listeners = new HashMap<>(1);
        listeners.put(filter, listener);
        initRegister(client, listeners);
    }

    private void initRegister(EunomiaClient client, Map<String, EunomiaListener> listeners) {
        if (listeners != null && !listeners.isEmpty()) {
            for (Map.Entry<String, EunomiaListener> listener : listeners.entrySet()) {
                if (!client.hasListener(listener.getKey())) {
                    client.addListener(listener.getKey(), listener.getValue());
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initRegister(client, listeners);
    }

    public void registerListeners(Map<String, EunomiaListener> listeners) {
        if (listeners != null && !listeners.isEmpty()) {
            if (this.listeners == null) {
                this.listeners = new HashMap<>();
            }
            this.listeners.putAll(listeners);
            initRegister(client, listeners);
        }
    }

    public void registerListener(String filter, EunomiaListener listener) {
        if (this.listeners == null) {
            this.listeners = new HashMap<>();
        }
        this.listeners.put(filter, listener);
        initRegister(client, filter, listener);
    }
}
