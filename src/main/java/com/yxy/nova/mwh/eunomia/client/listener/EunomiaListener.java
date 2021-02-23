package com.yxy.nova.mwh.eunomia.client.listener;

import com.yxy.nova.mwh.eunomia.client.message.RowData;

/**
 * Created by toruneko on 2016/12/7.
 */
public interface EunomiaListener {

    boolean onEvent(RowData rowData) throws Exception;

}
