package com.yxy.nova.mwh.elasticsearch.basic.select;

import com.yxy.nova.mwh.elasticsearch.basic.where.GroupWhere;

import java.util.Collection;

public interface WhereBuilder<T> {
    T whereEquals(String key, Object value);

    T whereNotEquals(String key, Object value);

    T whereStartsWith(String key, Object value);

    T whereGreater(String key, Object value);

    <E> T whereIn(String key, Collection<E> values);

    T whereGreaterOrEqual(String key, Object value);

    T whereLess(String key, Object value);

    T whereLessOrEqual(String key, Object value);

    T whereContains(String key, Object value);

    T whereNull(String key);

    T whereNotNull(String key);

    GroupWhere whereOr();

    GroupWhere whereAnd();
}
