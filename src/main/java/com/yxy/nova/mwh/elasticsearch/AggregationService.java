package com.yxy.nova.mwh.elasticsearch;

import com.yxy.nova.mwh.elasticsearch.basic.agg.bucket.Bucket;
import com.yxy.nova.mwh.elasticsearch.basic.agg.metrics.Metric;
import com.yxy.nova.mwh.elasticsearch.basic.where.WhereCondition;
import com.yxy.nova.mwh.elasticsearch.dto.agg.AggResult;
import com.yxy.nova.mwh.elasticsearch.dto.agg.MetricResult;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;

import java.util.List;
import java.util.Map;

/**
 * 集合查询专用接口
 * @author yuanwen.qu
 * @version 2020-02-28
 */
public interface AggregationService extends SearchService {


    /**
     * 分桶（分组）聚合查询
     * @param table
     * @param conditions
     * @param buckets
     * @param metrics
     * @return
     * @throws ElasticsearchClientException
     */
    Map<String, AggResult> aggregateQueryByBucket(String table, List<WhereCondition> conditions, List<Bucket> buckets, List<Metric> metrics) throws ElasticsearchClientException;

    /**
     * 聚合指标
     * @param table
     * @param conditions
     * @param metrics
     * @return
     * @throws ElasticsearchClientException
     */
    Map<String, MetricResult> aggregateQuery(String table, List<WhereCondition> conditions, List<Metric> metrics) throws ElasticsearchClientException;

}
