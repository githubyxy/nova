package com.yxy.nova.mwh.elasticsearch.basic.where;

/**
 * 用于模糊查询 不需要使用%来作为模糊匹配。意思是查询包含value的所有记录。
 */
public class MultiWhereLike implements WhereCondition {

    private String query;
    private String[] fields;

    public MultiWhereLike(String query, String... fields) {
        this.query = query;
        this.fields = fields;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public Object getValue() {
        return query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String... fields) {
        this.fields = fields;
    }
}
