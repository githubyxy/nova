package com.yxy.nova.mwh.eunomia.recipes;

import com.yxy.nova.mwh.eunomia.client.listener.EunomiaListener;
import com.yxy.nova.mwh.eunomia.client.message.RowData;
import com.yxy.nova.mwh.eunomia.recipes.message.Row;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by toruneko on 2017/4/14.
 */
public abstract class EunomiaListenerSupport implements EunomiaListener {

    private static final Logger logger = LoggerFactory.getLogger(EunomiaListener.class);

    private boolean logOriginalRowData = false;

    public boolean isLogOriginalRowData() {
        return logOriginalRowData;
    }

    public void setLogOriginalRowData(boolean logOriginalRowData) {
        this.logOriginalRowData = logOriginalRowData;
    }

    @Override
    public boolean onEvent(RowData rowData) throws Exception {
        if (isLogOriginalRowData()) {
            logger.info(JSON.toJSONString(rowData, SerializerFeature.DisableCircularReferenceDetect));
        }
        if (StringUtils.equalsIgnoreCase(rowData.getEventType(), "INSERT")) {
            return onInsertEvent(new Row(rowData));
        }

        if (StringUtils.equalsIgnoreCase(rowData.getEventType(), "UPDATE")) {
            return onUpdateEvent(new Row(rowData));
        }

        if (StringUtils.equalsIgnoreCase(rowData.getEventType(), "DELETE")) {
            return onDeleteEvent(new Row(rowData));
        }

        return onUnknownEvent(new Row(rowData));
    }

    abstract protected boolean onInsertEvent(Row row);

    abstract protected boolean onUpdateEvent(Row row);

    abstract protected boolean onDeleteEvent(Row row);

    protected boolean onUnknownEvent(Row row) {
        logger.warn("schema: {}, table: {}, eventType: {}", row.getSchemaName(), row.getTableName(),
                    row.getEventType());
        return true;
    }
}
