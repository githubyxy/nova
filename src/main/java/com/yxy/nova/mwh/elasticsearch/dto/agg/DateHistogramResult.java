package com.yxy.nova.mwh.elasticsearch.dto.agg;
/**
 * 时间统计结果
 * @author quyuanwen
 * @version 2020-03-02
 */
public class DateHistogramResult extends BucketResult {
    private String formatName;

    public DateHistogramResult(String key, int count) {
        super(key, count);
    }

    public String getFormatName() {
        return formatName;
    }

    public void setFormatName(String formatName) {
        this.formatName = formatName;
    }

    @Override
    public String toString() {
        return "DateHistogramResult{" + super.toString() + ", formatName='" + formatName + '\'' + '}';
    }
}
