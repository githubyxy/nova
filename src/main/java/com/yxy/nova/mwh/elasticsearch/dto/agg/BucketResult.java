package com.yxy.nova.mwh.elasticsearch.dto.agg;

import java.util.HashMap;
import java.util.Map;

/**
 * 桶结果
 * @author quyuanwen
 * @version 2020-03-02
 */
public class BucketResult {

    /**
     * 桶值key
     */
    private String key;
    /**
     * 总数
     */
    private int count ;

    /**
     * key为计算的别名
     */
    private Map<String, MetricResult> metricResults = new HashMap<>();

    /**
     * key为桶的别名
     */
    private Map<String, AggResult> subAggResultMap;

    public BucketResult(String key, int count) {
        this.key = key;
        this.count = count;
    }

    public String getKey() {
        return key;
    }

    public int getCount() {
        return count;
    }

    public Map<String, MetricResult> getMetricResults() {
        return this.metricResults;
    }

    public void setMetricResults(Map<String, MetricResult> metricResults) {
        this.metricResults = metricResults;
    }

    public Map<String, AggResult> getSubAggResultMap() {
        return subAggResultMap;
    }

    public void setSubAggResultMap(Map<String, AggResult> subAggResultMap) {
        this.subAggResultMap = subAggResultMap;
    }

    @Override
    public String toString() {
        return "BucketResult{" + "key='" + key + '\'' + ", count=" + count + ", metricResults=" + metricResults + ", subAggResultMap=" + subAggResultMap + '}';
    }
}
