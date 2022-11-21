package com.yxy.nova.mwh.eunomia.recipes.bulk;

import com.yxy.nova.mwh.eunomia.client.listener.EunomiaListener;
import com.yxy.nova.mwh.eunomia.client.message.RowData;

import java.util.Collection;

/**
 * Created by toruneko on 2017/4/18.
 */
public interface EunomiaBulkListener extends EunomiaListener {

    void onEvent(Collection<RowData> messages) throws Exception;

}
