package com.yxy.nova.mwh.elasticsearch.basic.agg.metrics;

import com.yxy.nova.mwh.elasticsearch.util.enumerate.MetricType;
import com.alibaba.fastjson.JSONObject;

/**
 * 求最小值
 * @author quyuanwen
 * @version 2020-02-28
 */
public class Min extends Metric{

    public Min(String name, String field) {
        super(MetricType.MIN, name, field);
    }

    @Override
    public JSONObject assemble(String field) {
        JSONObject content = new JSONObject();
        JSONObject fieldJson = new JSONObject();
        fieldJson.put("field", field);
        content.put("min", fieldJson);
        return content;
    }
}
