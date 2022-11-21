package com.yxy.nova.mwh.elasticsearch.basic.where;

/**
 * 用于表示不匹配
 * @author quyuanwen
 * @version 2020-02-06
 */
public class WhereNotEquals implements WhereCondition {

    private String key;
    private Object value;

    public WhereNotEquals(String key, Object value){
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
