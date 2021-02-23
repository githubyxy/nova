package com.yxy.nova.mwh.elasticsearch.basic.where;

/**
 *
 * @author quyuanwen
 * @version 2020-02-06
 */
public class WildcardWhereLike implements WhereCondition {

    private String key;
    private String value;

    public WildcardWhereLike(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
