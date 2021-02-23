package com.yxy.nova.mwh.elasticsearch.basic.where;

/**
 * 为空
 * @author quyuanwen
 * @version 2020-02-06
 */
public class WhereIsNull implements WhereCondition {

    private String key;

    public WhereIsNull(String key){
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Object getValue() {
        return null;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
