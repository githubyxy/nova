package com.yxy.nova.mwh.elasticsearch.basic.agg.bucket;

import com.alibaba.fastjson.JSONObject;

/**
 * 数字直方图分桶
 * @author quyuanwen
 * @version 2020-02-28
 */
public class Histogram extends Bucket {
    private Number interval;

    private Number offset;

    private Number min;

    private Number max;

    public Histogram(String name, String field, Number interval) {
        super(name, field);
        this.interval = interval;
    }

    @Override
    public JSONObject assemble(String field) {
        JSONObject content = new JSONObject();
        JSONObject histogram = new JSONObject();
        histogram.put("field", field);
        histogram.put("interval", this.interval);
        if(this.max != null && this.min != null){
            JSONObject extendedBounds = new JSONObject();
            extendedBounds.put("min", this.min);
            extendedBounds.put("max", this.max);
            histogram.put("extended_bounds", extendedBounds);
        }
        if(this.offset != null ){
            histogram.put("offset", offset);
        }
        content.put("histogram", histogram);
        return content;
    }

    public Histogram min(Number min) {
        this.min = min;
        return this;
    }

    public Histogram max(Number max) {
        this.max = max;
        return this;
    }

    public Histogram offset(Number offset) {
        this.offset = offset;
        return this;
    }
}