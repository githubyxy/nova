package com.yxy.nova.mwh.eunomia.recipes.message;

import com.yxy.nova.mwh.eunomia.client.message.RowData;

/**
 * Created by toruneko on 2017/4/17.
 */
public class Row {

    private String schemaName;

    private String tableName;

    private String eventType;

    private long executeTime;

    private FieldReader reader;

    public Row(RowData rowData) {
        this.schemaName = rowData.getSchemaName();
        this.tableName = rowData.getTableName();
        this.executeTime = rowData.getExecuteTime();
        this.eventType = rowData.getEventType().toUpperCase();
        this.reader = new FieldReader(rowData.getPrimary(), rowData.getColumns());
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getEventType() {
        return eventType;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public FieldReader getReader() {
        return reader;
    }
}
