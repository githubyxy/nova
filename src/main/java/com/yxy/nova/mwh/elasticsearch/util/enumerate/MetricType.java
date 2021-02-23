package com.yxy.nova.mwh.elasticsearch.util.enumerate;

/**
 * 统计类型
 * @author quyuanwen
 * @version 2020-03-02
 */
public enum MetricType {
    COUNT("count","条数"),
    MIN("min","最小值"),
    MAX("max","最大值"),
    AVG("avg","平均"),
    SUM("sum","求和"),
    STATS("stats","统计");

    private String code;
    private String desc;
    MetricType(String code, String desc){
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