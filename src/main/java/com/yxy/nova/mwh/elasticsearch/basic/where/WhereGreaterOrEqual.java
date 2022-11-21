package com.yxy.nova.mwh.elasticsearch.basic.where;

/**
 * 大于等于
 * @author quyuanwen
 * @version 2020-02-06
 */
public class WhereGreaterOrEqual implements WhereCondition {

    private String key;
    private Object value;

    public WhereGreaterOrEqual(String key, Object value){
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
