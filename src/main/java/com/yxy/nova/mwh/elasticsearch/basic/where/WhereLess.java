package com.yxy.nova.mwh.elasticsearch.basic.where;

/**
 * 小于
 * @author quyuanwen
 * @version 2020-02-06
 */
public class WhereLess implements WhereCondition {

    private String key;
    private Object value;

    public WhereLess(String key, Object value){
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

    public void setValue(Object value) {
        this.value = value;
    }
}
