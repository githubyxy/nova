package com.yxy.nova.mwh.elasticsearch.dto.agg;
/**
 * 范围统计结果
 * @author quyuanwen
 * @version 2020-03-02
 */
public class RangeResult extends BucketResult {

    private Object from;

    private Object to;

    public RangeResult(String key, int count) {
        super(key, count);
    }

    public Object getFrom() {
        return from;
    }

    public void setFrom(Object from) {
        this.from = from;
    }

    public Object getTo() {
        return to;
    }

    public void setTo(Object to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "RangeResult{" + super.toString() + ",from=" + from + ", to=" + to + '}';
    }
}
