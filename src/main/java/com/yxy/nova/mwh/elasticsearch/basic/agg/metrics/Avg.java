package com.yxy.nova.mwh.elasticsearch.basic.agg.metrics;

import com.yxy.nova.mwh.elasticsearch.util.enumerate.MetricType;
import com.alibaba.fastjson.JSONObject;

/**
 * 求均值
 * @author quyuanwen
 * @version 2020-02-28
 */
public class Avg extends Metric{
    public Avg(String name, String field) {
        super(MetricType.AVG, name, field);
    }

    @Override
    public JSONObject assemble(String field) {
        JSONObject content = new JSONObject();
        JSONObject fieldJson = new JSONObject();
        fieldJson.put("field", field);
        content.put("avg", fieldJson);
        return content;
    }
}
