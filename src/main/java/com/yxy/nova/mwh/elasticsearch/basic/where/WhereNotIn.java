package com.yxy.nova.mwh.elasticsearch.basic.where;

import java.util.Collection;

/**
 * 多值不匹配
 * @author quyuanwen
 * @version 2020-02-06
 */
public class WhereNotIn<T> implements WhereCondition<Collection<T>> {

    private String        key;
    private Collection<T> values;

    public WhereNotIn(String key, Collection<T> values) {
        this.key = key;
        this.values = values;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public Collection<T> getValue() {
        return values;
    }

    public void setValues(Collection<T> values) {
        this.values = values;
    }
}
