package com.yxy.nova.mwh.elasticsearch.basic.select;

import com.yxy.nova.mwh.elasticsearch.basic.where.WhereCondition;

/**
 * Created by caipeichao on 2015/1/22.
 */
public class Limit implements WhereCondition {

    private int from;
    private int size;

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Limit(int from, int size){
        this.from = from;
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
