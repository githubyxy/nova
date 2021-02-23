package com.yxy.nova.mwh.eunomia.client.message;

import com.alibaba.fastjson.annotation.JSONField;

import java.sql.Types;

/**
 * Created by toruneko on 2016/12/6.
 */
public class Column {

    /**
     * 字段序号
     */
    @JSONField(name = "index")
    private int index;

    /**
     * 字段名
     */
    @JSONField(name = "name")
    private String name;

    /**
     * @see Types
     */
    @JSONField(name = "sql_type")
    private int sqlType;

    /**
     * 更新前的值
     */
    @JSONField(name = "before_value")
    private String beforeValue;

    /**
     * 更新后的值
     */
    @JSONField(name = "after_value")
    private String afterValue;

    /**
     * 是否是主键
     */
    @JSONField(name = "is_key")
    private boolean isKey;

    /**
     * 是否发生过变更
     */
    @JSONField(name = "updated")
    private boolean updated;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSqlType() {
        return sqlType;
    }

    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

    public String getBeforeValue() {
        return beforeValue;
    }

    public void setBeforeValue(String beforeValue) {
        this.beforeValue = beforeValue;
    }

    public String getAfterValue() {
        return afterValue;
    }

    public void setAfterValue(String afterValue) {
        this.afterValue = afterValue;
    }

    public boolean isKey() {
        return isKey;
    }

    public void setKey(boolean key) {
        isKey = key;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
}
