package com.yxy.nova.mwh.elasticsearch.basic.select;

import com.yxy.nova.mwh.elasticsearch.basic.where.WhereCondition;

import java.util.List;

public class GroupByRanges implements WhereCondition {

    private String                 name;
    private String                 field;
    private List<? extends Number> ranges;

    public GroupByRanges(String name, String field, List<? extends Number> ranges) {
        this.name = name;
        this.field = field;
        this.ranges = ranges;
    }

    public String getName() {
        return name;
    }

    public String getField() {
        return field;
    }

    public List<? extends Number> getRanges() {
        return ranges;
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
