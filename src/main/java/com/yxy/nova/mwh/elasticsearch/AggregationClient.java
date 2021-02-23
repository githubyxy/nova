package com.yxy.nova.mwh.elasticsearch;

import com.yxy.nova.mwh.elasticsearch.basic.agg.AggregationBuilder;
import com.yxy.nova.mwh.elasticsearch.basic.agg.bucket.Bucket;
import com.yxy.nova.mwh.elasticsearch.basic.agg.bucket.DateHistogram;
import com.yxy.nova.mwh.elasticsearch.basic.agg.bucket.Range;
import com.yxy.nova.mwh.elasticsearch.basic.agg.metrics.Metric;
import com.yxy.nova.mwh.elasticsearch.basic.where.WhereCondition;
import com.yxy.nova.mwh.elasticsearch.dto.SearchRequest;
import com.yxy.nova.mwh.elasticsearch.dto.agg.*;
import com.yxy.nova.mwh.elasticsearch.exception.ESExceptionType;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.util.GenerateSearchRequest;
import com.yxy.nova.mwh.elasticsearch.util.enumerate.MetricType;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 执行聚合查询
 * @author quyuanwen
 * @version 2020-03-01
 */
public class AggregationClient extends ElasticsearchClient implements AggregationService {
    private Logger logger = LoggerFactory.getLogger(AggregationClient.class);


    @Override
    public Map<String, AggResult> aggregateQueryByBucket(String table, List<WhereCondition> conditions, List<Bucket> buckets, List<Metric> metrics) throws ElasticsearchClientException {
        //记录rt
//        MetricTimer.Context timer = this.monitorGenerator.monitorRT(table, "aggregateQueryByBucket");
        //记录tps
//        this.monitorGenerator.monitorTPS(table, "aggregateQueryByBucket");
        // 生成请求
        SearchRequest request = null;
        try {
            List<String> indices = policy.getReadIndices(table, conditions, false);
            if (CollectionUtils.isEmpty(indices)) {
                throw new ElasticsearchClientException("获取索引信息为空", ESExceptionType.INDEX_ERROR);
            }
            request = GenerateSearchRequest.buildRequest(indices.toArray(new String[indices.size()]), table, conditions);

            if(!CollectionUtils.isEmpty(buckets)){
                request.putBody(AggregationBuilder.getBucketSentence(buckets, metrics));
            }else{
                throw new ElasticsearchClientException("分桶信息为空", ESExceptionType.AGGREGATION_BUCKET_ERROR);
            }

            // 不需要具体的数据
            request.putQueryString("from", "0");
            request.putQueryString("size", "0");

            // 发送请求
            JSONObject response = search(request);
            // 解析请求
            JSONObject aggregations = response.getJSONObject("aggregations");
            if (aggregations == null){
                return new HashMap<>();
            }
            Map<String, AggResult> resultMap = new HashMap<>();

            //解析聚合结果
            this.extractAggregation(aggregations, buckets, resultMap);
            if(!CollectionUtils.isEmpty(metrics)){
                //解析聚合结果
                Map<String, MetricResult> metricResultMap = this.extractMetricContent(aggregations, metrics);
                if(metricResultMap != null && !metricResultMap.isEmpty()){
                    for(MetricResult metricResult : metricResultMap.values()){
                        resultMap.put(metricResult.getName(), new AggResult(metricResult));
                    }
                }
            }
            //打点
//            if (timer != null) {
//                timer.stop();
//            }
            return resultMap;
        }catch (Exception e){
            logger.error("【分桶聚合】分桶聚合失败！请求信息{} ", request, e);
            //监控异常
//            this.monitorGenerator.countException(table, "aggregateQueryByBucket", e.getClass().getName());
            throw e;
        }
    }

    @Override
    public Map<String, MetricResult> aggregateQuery(String table, List<WhereCondition> conditions, List<Metric> metrics) throws ElasticsearchClientException {
        //记录rt
//        MetricTimer.Context timer = this.monitorGenerator.monitorRT(table, "aggregateQuery");
        //记录tps
//        this.monitorGenerator.monitorTPS(table, "aggregateQuery");
        // 生成请求
        SearchRequest request = null;
        try {
            List<String> indices = policy.getReadIndices(table, conditions, false);
            if (CollectionUtils.isEmpty(indices)) {
                throw new ElasticsearchClientException("获取索引信息为空", ESExceptionType.INDEX_ERROR);
            }
            request = GenerateSearchRequest.buildRequest(indices.toArray(new String[indices.size()]), table, conditions);

            if(!CollectionUtils.isEmpty(metrics)){
                request.putBody(AggregationBuilder.getMetricSentence(metrics));
            }else{
                throw new ElasticsearchClientException("计算指标信息为空", ESExceptionType.AGGREGATION_METRIC_ERROR);
            }

            // 不需要具体的数据
            request.putQueryString("from", "0");
            request.putQueryString("size", "0");

            // 发送请求
            JSONObject response = search(request);
            // 解析请求
            JSONObject aggregations = response.getJSONObject("aggregations");
            if (aggregations == null){
                return new HashMap<>();
            }

            //解析计算结果
            Map<String, MetricResult> resultMap =  this.extractMetricContent(aggregations, metrics);
            //打点
//            if (timer != null) {
////                timer.stop();
////            }
            return resultMap;
        }catch (Exception e){
            logger.error("【聚合计算】聚合计算失败！请求信息{} ", request, e);
            //监控异常
//            this.monitorGenerator.countException(table, "aggregateQuery", e.getClass().getName());
            throw e;
        }
    }

    /**
     * 接续聚合结果
     * @param aggregations
     * @param buckets
     * @param resultMap
     */
    private void extractAggregation(JSONObject aggregations, List<Bucket> buckets, Map<String, AggResult> resultMap){
        for(Bucket bucket : buckets){
            JSONObject aggs = aggregations.getJSONObject(bucket.getName());
            if (aggs != null && !aggs.isEmpty()){
                //记录结果
                AggResult aggResult = new AggResult(bucket.getName());
                resultMap.put(bucket.getName(), aggResult);
                //获取所有分桶
                JSONArray bucketContents = aggs.getJSONArray("buckets");
                for(int i = 0; i < bucketContents.size(); i++){
                    JSONObject content = bucketContents.getJSONObject(i);
                    BucketResult bucketResult = this.extractBucketContent(content, bucket);
                    aggResult.addBucketResult(bucketResult);
                }
            }
        }

    }

    /**
     * 解析桶结果
     * @param content
     * @param bucket
     * @return
     */
    private BucketResult extractBucketContent(JSONObject content, Bucket bucket){
        String key = content.getString("key");
        int count = content.getIntValue("doc_count");
        BucketResult bucketResult;
        if(bucket instanceof DateHistogram){
            String name = content.getString("key_as_string");
            bucketResult = new DateHistogramResult(key, count);
            if(!StringUtils.isEmpty(name)){
                ((DateHistogramResult)bucketResult).setFormatName(name);
            }
        }else if(bucket instanceof Range){
            String from = content.getString("from");
            String to = content.getString("to");
            bucketResult = new RangeResult(key, count);
            if(!StringUtils.isEmpty(from)){
                ((RangeResult)bucketResult).setFrom(from);
            }
            if(!StringUtils.isEmpty(to)){
                ((RangeResult)bucketResult).setTo(to);
            }
        }else{
            bucketResult = new BucketResult(key, count);
        }
        //解析计算结果
        if(!CollectionUtils.isEmpty(bucket.getMetrics())){
            bucketResult.setMetricResults(this.extractMetricContent(content, bucket.getMetrics()));
        }
        if(bucket.getSubs() != null){
            Map<String, AggResult> resultMap = new HashMap<>();
            bucketResult.setSubAggResultMap(resultMap);
            this.extractAggregation(content, bucket.getSubs(), resultMap);
        }
        return bucketResult;
    }

    /**
     * 解释计算指标结果
     * @param content
     * @param metrics
     */
    private Map<String, MetricResult> extractMetricContent(JSONObject content, List<Metric> metrics){
        Map<String, MetricResult> metricResults = new HashMap<>();
        for(Metric metric : metrics){
            JSONObject metricContent = content.getJSONObject(metric.getName());
            if(metricContent != null && !metricContent.isEmpty()){
                if(metric.getType() == MetricType.STATS){
                    Object value = metricContent.get(MetricType.COUNT);
                    MetricResult metricResult = new MetricResult(metric.getName(), metric.getType(), value);
                    metricResult.addMetricValue(MetricType.COUNT, value);
                    value = metricContent.get(MetricType.MAX);
                    metricResult.addMetricValue(MetricType.MAX, value);
                    value = metricContent.get(MetricType.MIN);
                    metricResult.addMetricValue(MetricType.MIN, value);
                    value = metricContent.get(MetricType.SUM);
                    metricResult.addMetricValue(MetricType.SUM, value);
                    value = metricContent.get(MetricType.AVG);
                    metricResult.addMetricValue(MetricType.AVG, value);
                }else{
                    Object value = metricContent.get("value");
                    metricResults.put(metric.getName(), new MetricResult(metric.getName(), metric.getType(), value));
                }
            }
        }
        return metricResults;
    }
}

