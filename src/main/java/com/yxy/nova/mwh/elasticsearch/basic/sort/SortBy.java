package com.yxy.nova.mwh.elasticsearch.basic.sort;

import com.yxy.nova.mwh.elasticsearch.basic.where.WhereCondition;

/**
 * Created by caipeichao on 2015/1/22.
 */
public class SortBy implements WhereCondition {

    private String   field;

    private SortMode sortMode;

    public SortBy(String field, SortMode sortMode){
        this.field = field;
        this.sortMode = sortMode;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public SortMode getSortMode() {
        return sortMode;
    }

    public void setSortMode(SortMode sortMode) {
        this.sortMode = sortMode;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public Object getValue() {
        return null;
    }
}
