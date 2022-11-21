package com.yxy.nova.mwh.elasticsearch.basic.where;

/**
 * 用于表示匹配 <br>
 * 分词和不分词 通过字段key是否以.raw结尾来决定
 * @author quyuanwen
 * @version 2020-02-06
 */
public class WhereEquals implements WhereCondition {

    private String key;
    private Object value;

    public WhereEquals(String key, Object value){
        this.key = key;
        this.value = value;
    }
    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

