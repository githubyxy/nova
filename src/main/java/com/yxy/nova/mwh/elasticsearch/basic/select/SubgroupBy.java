package com.yxy.nova.mwh.elasticsearch.basic.select;

import com.yxy.nova.mwh.elasticsearch.basic.where.WhereCondition;

/**
 * Created by caipeichao on 2015/1/22.
 */
public class SubgroupBy implements WhereCondition {

    private String name;
    private String field;
    private int    size;

    public SubgroupBy(String name, String field, int size){
        this.name = name;
        this.field = field;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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
