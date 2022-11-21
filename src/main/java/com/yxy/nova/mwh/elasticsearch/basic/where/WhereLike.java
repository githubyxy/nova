package com.yxy.nova.mwh.elasticsearch.basic.where;

/**
 * 用于模糊查询 不需要使用%来作为模糊匹配。意思是查询包含value的所有记录。
 * @author quyuanwen
 * @version 2020-02-06
 */
public class WhereLike implements WhereCondition {

    private String key;
    private String value;

    public WhereLike(String key, String value){
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
