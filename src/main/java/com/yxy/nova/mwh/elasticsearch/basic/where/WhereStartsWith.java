package com.yxy.nova.mwh.elasticsearch.basic.where;

/**
 * 前缀匹配
 * @author quyuanwen
 * @version 2020-02-06
 */
public class WhereStartsWith implements WhereCondition {

    private String key;
    private Object value;

    public WhereStartsWith(String key, Object value){
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

    public void setValue(String value) {
        this.value = value;
    }
}
