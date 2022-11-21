package com.yxy.nova.mwh.eunomia.recipes.dispatcher;

import com.yxy.nova.mwh.eunomia.client.listener.EunomiaListener;
import com.yxy.nova.mwh.eunomia.client.message.RowData;

import java.util.Map;

/**
 * Created by toruneko on 2017/5/10.
 */
public class EunomiaListenerDispatcher implements EunomiaListener {

    private Map<String, EunomiaListener> listeners;

    public void setListeners(Map<String, EunomiaListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public boolean onEvent(RowData rowData) throws Exception {
        if (listeners == null || listeners.isEmpty()) {
            return false;
        }

        String filter = getFiltered(rowData);
        if (!listeners.containsKey(filter)) {
            return true;
        }

        EunomiaListener listener = listeners.get(filter);
        return listener.onEvent(rowData);
    }

    private String getFiltered(RowData rowData) {
        return rowData.getSchemaName() + "." + rowData.getTableName();
    }
}
