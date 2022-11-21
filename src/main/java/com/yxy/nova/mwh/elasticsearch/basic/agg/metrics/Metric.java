package com.yxy.nova.mwh.elasticsearch.basic.agg.metrics;

import com.yxy.nova.mwh.elasticsearch.util.enumerate.MetricType;
import com.alibaba.fastjson.JSONObject;

/**
 * 指标计算
 * @author quyuanwen
 * @version 2020-02-28
 */
public abstract class Metric {

    private MetricType type;

    private String name;

    private String field;

    public Metric(MetricType type, String name, String field) {
        this.type = type;
        this.name = name;
        this.field = field;
    }

    public JSONObject getMetricPart(){
        JSONObject agg = new JSONObject();
        JSONObject content = assemble(this.field);
        if(content != null && !content.isEmpty()){
            agg.put(name, content);
            return agg;
        }
        return null;
    }
    /**
     * 组装语句
     * @return
     */
    public abstract JSONObject assemble(String field);

    public MetricType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getField() {
        return field;
    }
}
