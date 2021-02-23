package com.yxy.nova.mwh.elasticsearch.util.enumerate;

/**
 * 时间单位
 * @author quyuanwen
 * @version 2020-03-01
 */
public enum TimeUnit {
    MILLISECOND("ms","毫秒"),
    SECOND("s","秒"),
    MINUTE("m","分钟"),
    HOUR("h","小时"),
    DAY("d","天"),
    WEEK("w","周"),
    MONTH("M","月"),
    QUARTER("q","季度"),
    YEAR("y","年"),;

    private String code;
    private String desc;

    TimeUnit(String code, String desc){
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
