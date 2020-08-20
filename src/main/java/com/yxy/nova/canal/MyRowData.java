package com.yxy.nova.canal;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by toruneko on 2016/12/6.
 */
public class MyRowData {

    /**
     * 数据库名
     */
    @JSONField(name = "schema_name")
    private String schemaName;

    /**
     * 表名
     */
    @JSONField(name = "table_name")
    private String tableName;

    /**
     * 事件类型 INSERT/DELETE/UPDATE
     */
    @JSONField(name = "event_type")
    private String eventType;

    /**
     * 执行时间
     */
    @JSONField(name = "execute_time")
    private long executeTime;

    /**
     * 主键
     */
    @JSONField(name = "primary")
    private MyColumn primary;

    /**
     * 变化的记录
     */
    @JSONField(name = "columns")
    private List<MyColumn> columns;

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
    }

    public MyColumn getPrimary() {
        return primary;
    }

    public void setPrimary(MyColumn primary) {
        this.primary = primary;
    }

    public List<MyColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<MyColumn> columns) {
        this.columns = columns;
    }
}
