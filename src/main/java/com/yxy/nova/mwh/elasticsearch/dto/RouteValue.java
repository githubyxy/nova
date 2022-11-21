package com.yxy.nova.mwh.elasticsearch.dto;

/**
 * 路由字段的值信息，目前只真对单值对应，不支持范围、模糊
 * @author quyuanwen
 */
public class RouteValue {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
