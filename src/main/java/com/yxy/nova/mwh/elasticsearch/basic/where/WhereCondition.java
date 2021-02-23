package com.yxy.nova.mwh.elasticsearch.basic.where;

/**
 * @author quyuanwen
 * @version 2020-02-06
 */
public interface WhereCondition<T> {

    String getKey();

    T getValue();
}
