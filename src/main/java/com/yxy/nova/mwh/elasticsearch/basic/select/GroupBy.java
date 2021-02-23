package com.yxy.nova.mwh.elasticsearch.basic.select;

import com.yxy.nova.mwh.elasticsearch.basic.where.WhereCondition;
import com.yxy.nova.mwh.elasticsearch.util.enumerate.AggType;

/**
 * 用于聚合统计结果 <br>
 * name ： 聚合标识 <br>
 * key ： 表示聚合的字段 <br>
 * size ： 表示取出聚合后前n组聚合内容 <br>
 */
public class GroupBy implements WhereCondition {

    private String name;
    private String field;
    private int size;
    private AggType aggType;

    public GroupBy(String name, String field, int size) {
        this.name = name;
        this.field = field;
        this.size = size;
    }

    public GroupBy(String name, String field, AggType aggType, int size) {
        this.name = name;
        this.field = field;
        this.aggType = aggType;
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

    public void setField(String key) {
        this.field = key;
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
