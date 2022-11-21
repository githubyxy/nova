package com.yxy.nova.mwh.eunomia.recipes.bulk;

import com.yxy.nova.mwh.eunomia.client.listener.EunomiaListenerConfig;

/**
 * Created by toruneko on 2017/4/18.
 */
public interface EunomiaBulkListenerConfig extends EunomiaListenerConfig {

    int bulkSize(int bulkSize);

    int bulkInterval(int bulkInterval);
}
