package com.yxy.nova.mwh.elasticsearch.basic.agg.bucket;

import com.yxy.nova.mwh.elasticsearch.util.enumerate.TimeUnit;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;


/**
 * 时间直方图分桶
 * @author quyuanwen
 * @version 2020-02-28
 */
public class DateHistogram extends Bucket {

    private int interval;

    private TimeUnit timeUnit;

    private String format;

    public DateHistogram(String name, String field, int interval, TimeUnit timeUnit, String format) {
        super(name, field);
        this.interval = interval;
        this.timeUnit = timeUnit;
        this.format = format;
    }

    @Override
    public JSONObject assemble(String field) {
        JSONObject content = new JSONObject();
        JSONObject dateHistogram = new JSONObject();
        dateHistogram.put("field", field);
        dateHistogram.put("interval", this.interval+timeUnit.getCode());
        if(!StringUtils.isEmpty(this.format)){
            dateHistogram.put("format", this.format);
        }
        content.put("date_histogram", dateHistogram);
        return content;
    }
}
