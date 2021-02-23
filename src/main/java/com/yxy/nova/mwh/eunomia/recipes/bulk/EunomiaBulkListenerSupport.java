package com.yxy.nova.mwh.eunomia.recipes.bulk;

import com.yxy.nova.mwh.eunomia.client.message.RowData;
import com.yxy.nova.mwh.eunomia.recipes.EunomiaListenerSupport;
import com.yxy.nova.mwh.kafka.object.RetryLaterException;

import java.util.Collection;

public abstract class EunomiaBulkListenerSupport extends EunomiaListenerSupport implements EunomiaBulkListener {

    @Override
    public void onEvent(Collection<RowData> messages) throws Exception {
        for (RowData message : messages) {
            if (!onEvent(message)) {
                throw new RetryLaterException();
            }
        }
    }
}
