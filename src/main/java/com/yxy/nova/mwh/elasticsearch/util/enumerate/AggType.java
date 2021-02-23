package com.yxy.nova.mwh.elasticsearch.util.enumerate;

/**
 * 聚合类型
 * @author quyuanwen
 * @version 2020-02-27
 */
public enum AggType {
    TERMS("terms","条件"),
    RANGE("range","范围"),
    STATS("stats","统计"),
    MIN("min","最小值"),
    MAX("max","最大值"),
    AVG("avg","平均"),
    SUM("sum","求和");

    private String code;
    private String desc;
    AggType(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}