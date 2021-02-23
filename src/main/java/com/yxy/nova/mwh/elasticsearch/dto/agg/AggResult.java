package com.yxy.nova.mwh.elasticsearch.dto.agg;

import java.util.HashMap;
import java.util.Map;

/**
 * 聚合结果
 * @author quyuanwen
 * @version 2020-03-02
 */
public class AggResult {

    /**
     * 聚合别名
     */
    private String name;
    /**
     * 计算结果
     */
    private MetricResult metricResult;
    /**
     * key为分桶的key
     */
    private Map<String, BucketResult> bucketResults;

    public AggResult(String name) {
        this.name = name;
    }

    public AggResult(MetricResult metricResult) {
        this.name = metricResult.getName();
        this.metricResult = metricResult;
    }

    public String getName() {
        return name;
    }

    public Map<String, BucketResult> getBucketResults() {
        return bucketResults;
    }

    /**
     * 添加桶计算结果
     * @param bucketResult
     */
    public void addBucketResult(BucketResult bucketResult) {
        if(this.bucketResults == null ){
            this.bucketResults = new HashMap<>();
        }
        this.bucketResults.put(bucketResult.getKey(), bucketResult);
    }

    public MetricResult getMetricResult() {
        return metricResult;
    }

    @Override
    public String toString() {
        return "AggResult{" + "name='" + name + '\'' + ", metricResult=" + metricResult + ", bucketResults=" + bucketResults + '}';
    }
}
