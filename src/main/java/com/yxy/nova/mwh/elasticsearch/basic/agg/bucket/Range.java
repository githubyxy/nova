package com.yxy.nova.mwh.elasticsearch.basic.agg.bucket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 范围分桶
 * @author quyuanwen
 * @version 2020-02-28
 */
public class Range extends Bucket{

    private List<RangeUnit> rangeUnits = new ArrayList<>();

    public Range(String name, String field) {
        super(name, field);
    }

    @Override
    public JSONObject assemble(String field) {
        JSONObject content = new JSONObject();
        JSONObject range = new JSONObject();
        range.put("field", field);
        range.put("ranges", JSON.toJSON(rangeUnits));
        content.put("range", range);
        return content;
    }


    public Range rangeUnit(String key, Number from, Number to){
        this.rangeUnits.add(new RangeUnit(key, from, to));
        return this;
    }


    private class RangeUnit{
        private String key;

        private Number from;

        private Number to;

        public RangeUnit(String key, Number from, Number to) {
            this.key = key;
            this.from = from;
            this.to = to;
        }

        public String getKey() {
            return key;
        }

        public Number getFrom() {
            return from;
        }

        public Number getTo() {
            return to;
        }
    }
}
