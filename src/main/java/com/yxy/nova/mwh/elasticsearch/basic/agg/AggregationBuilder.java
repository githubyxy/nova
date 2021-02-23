package com.yxy.nova.mwh.elasticsearch.basic.agg;

import com.yxy.nova.mwh.elasticsearch.AggregationService;
import com.yxy.nova.mwh.elasticsearch.basic.agg.bucket.*;
import com.yxy.nova.mwh.elasticsearch.basic.agg.metrics.*;
import com.yxy.nova.mwh.elasticsearch.basic.select.SearchBuilder;
import com.yxy.nova.mwh.elasticsearch.dto.agg.AggResult;
import com.yxy.nova.mwh.elasticsearch.dto.agg.MetricResult;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.util.enumerate.TimeUnit;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 聚合信息生成
 * @author quyuanwen
 * @version 2020-03-01
 */
public class AggregationBuilder extends SearchBuilder {

    private List<Bucket> buckets = new ArrayList<>();

    private List<Metric> metrics = new ArrayList<>();

    public AggregationBuilder(AggregationService service, String table) {
        super(service, table);
    }

    /**
     * 时间直方图分桶
     * @param name
     * @param field
     * @param interval
     * @param timeUnit
     * @param format
     * @return
     */
    public DateHistogram buildDateHistogram(String name, String field, int interval, TimeUnit timeUnit, String format){
        DateHistogram dateHistogram = new DateHistogram(name, field, interval, timeUnit, format);
        this.buckets.add(dateHistogram);
        return dateHistogram;
    }
    /**
     * 时间直方图分桶
     * @param name
     * @param field
     * @param interval
     * @param timeUnit
     * @return
     */
    public DateHistogram buildDateHistogram(String name, String field, int interval, TimeUnit timeUnit){
        DateHistogram dateHistogram = new DateHistogram(name, field, interval, timeUnit, null);
        this.buckets.add(dateHistogram);
        return dateHistogram;
    }

    /**
     * 数据直方图分桶
     * @param name
     * @param field
     * @param interval
     * @return
     */
    public Histogram buildHistogram(String name, String field, Number interval){
        Histogram histogram = new Histogram(name, field, interval);
        this.buckets.add(histogram);
        return histogram;
    }

    /**
     * 范围分桶
     * @param name
     * @param field
     * @return
     */
    public Range buildRange(String name, String field){
        Range range = new Range(name, field);
        this.buckets.add(range);
        return range;
    }

    /**
     * 字段值分桶
     * @param name
     * @param field
     * @return
     */
    public Terms buildTerms(String name, String field){
        Terms terms = new Terms(name, field);
        this.buckets.add(terms);
        return terms;
    }

    /**
     * 字段值分桶
     * @param name
     * @param field
     * @param size
     * @return
     */
    public Terms buildTerms(String name, String field, int size){
        Terms terms = new Terms(name, field, size);
        this.buckets.add(terms);
        return terms;
    }

    /**
     * 时间直方图分桶，创建子分桶
     * @param bucket
     * @param name
     * @param field
     * @param interval
     * @param timeUnit
     * @param format
     * @return
     */
    public DateHistogram buildDateHistogram(Bucket bucket, String name, String field, int interval, TimeUnit timeUnit, String format){
        DateHistogram dateHistogram = new DateHistogram(name, field, interval, timeUnit, format);
        bucket.sub(dateHistogram);
        return dateHistogram;
    }

    /**
     * 时间直方图分桶，创建子分桶
     * @param bucket
     * @param name
     * @param field
     * @param interval
     * @param timeUnit
     * @return
     */
    public DateHistogram buildDateHistogram(Bucket bucket, String name, String field, int interval, TimeUnit timeUnit){
        DateHistogram dateHistogram = new DateHistogram(name, field, interval, timeUnit, null);
        bucket.sub(dateHistogram);
        return dateHistogram;
    }

    /**
     * 数据直方图分桶，创建子分桶
     * @param name
     * @param field
     * @param interval
     * @return
     */
    public Histogram buildHistogram(Bucket bucket, String name, String field, Number interval){
        Histogram histogram = new Histogram(name, field, interval);
        bucket.sub(histogram);
        return histogram;
    }

    /**
     * 范围分桶，创建子分桶
     * @param name
     * @param field
     * @return
     */
    public Range buildRange(Bucket bucket, String name, String field){
        Range range = new Range(name, field);
        bucket.sub(range);
        return range;
    }

    /**
     * 字段值分桶，创建子分桶
     * @param name
     * @param field
     * @return
     */
    public Terms buildTerms(Bucket bucket, String name, String field){
        Terms terms = new Terms(name, field);
        bucket.sub(terms);
        return terms;
    }

    /**
     * 字段值分桶，创建子分桶
     * @param name
     * @param field
     * @param size
     * @return
     */
    public Terms buildTerms(Bucket bucket, String name, String field, int size){
        Terms terms = new Terms(name, field, size);
        bucket.sub(terms);
        return terms;
    }

    /**
     * 创建平均值计算指标
     * @param name
     * @param field
     * @return
     */
    public AggregationBuilder buildAvgMetric(String name, String field){
        Avg avg = new Avg(name, field);
        this.metrics.add(avg);
        return this;
    }

    /**
     * 创建最大值计算指标
     * @param name
     * @param field
     * @return
     */
    public AggregationBuilder buildMaxMetric(String name, String field){
        Max max = new Max(name, field);
        this.metrics.add(max);
        return this;
    }

    /**
     * 创建最小值计算指标
     * @param name
     * @param field
     * @return
     */
    public AggregationBuilder buildMinMetric(String name, String field){
        Min min = new Min(name, field);
        this.metrics.add(min);
        return this;
    }

    /**
     * 创建求和计算指标
     * @param name
     * @param field
     * @return
     */
    public AggregationBuilder buildSumMetric(String name, String field){
        Sum sum = new Sum(name, field);
        this.metrics.add(sum);
        return this;
    }

    /**
     * 创建计数、求和、最大值、最小值、平均值计算指标
     * @param name
     * @param field
     * @return
     */
    public AggregationBuilder buildStatsMetric(String name, String field){
        Stats stats = new Stats(name, field);
        this.metrics.add(stats);
        return this;
    }


    /**
     * 创建平均值计算指标
     * @param name
     * @param field
     * @return
     */
    public Bucket buildAvgMetric(Bucket bucket, String name, String field){
        Avg avg = new Avg(name, field);
        bucket.metric(avg);
        return bucket;
    }
    /**
     * 创建最大值计算指标
     * @param name
     * @param field
     * @return
     */
    public Bucket buildMaxMetric(Bucket bucket, String name, String field){
        Max max = new Max(name, field);
        bucket.metric(max);
        return bucket;
    }

    /**
     * 创建最小值计算指标
     * @param name
     * @param field
     * @return
     */
    public Bucket buildMinMetric(Bucket bucket, String name, String field){
        Min min = new Min(name, field);
        bucket.metric(min);
        return bucket;
    }

    /**
     * 创建求和计算指标
     * @param name
     * @param field
     * @return
     */
    public Bucket buildSumMetric(Bucket bucket, String name, String field){
        Sum sum = new Sum(name, field);
        bucket.metric(sum);
        return bucket;
    }

    /**
     * 创建计数、求和、最大值、最小值、平均值计算指标
     * @param name
     * @param field
     * @return
     */
    public Bucket buildStatsMetric(Bucket bucket, String name, String field){
        Stats stats = new Stats(name, field);
        bucket.metric(stats);
        return bucket;
    }

    /**
     * 分桶（分组）聚合查询
     * @return
     * @throws ElasticsearchClientException
     */
    public Map<String, AggResult> aggregateQueryByBucket() throws ElasticsearchClientException{
        return ((AggregationService) service).aggregateQueryByBucket(this.table, this.conditions, this.buckets, this.metrics);
    }

    /**
     * 聚合指标
     * @return
     * @throws ElasticsearchClientException
     */
    public Map<String, MetricResult> aggregateQuery() throws ElasticsearchClientException{
        return ((AggregationService) service).aggregateQuery(this.table, this.conditions, this.metrics);
    }

    /**
     * 获取聚合的json语法
     * @param buckets
     * @param metrics
     * @return
     */
    public static JSONObject getBucketSentence(List<Bucket> buckets, List<Metric> metrics){
        JSONObject sentence = new JSONObject();
        JSONObject agg = new JSONObject();
        for(Bucket bucket : buckets){
            agg.putAll(bucket.getBucketPart());
        }
        if(!CollectionUtils.isEmpty(metrics)){
            for(Metric metric : metrics){
                agg.putAll(metric.getMetricPart());
            }
        }
        sentence.put("aggs",agg);
        return sentence;
    }

    /**
     * 获取计算的json语法
     * @param metrics
     * @return
     */
    public static JSONObject getMetricSentence(List<Metric> metrics){
        JSONObject sentence = new JSONObject();
        JSONObject agg = new JSONObject();
        for(Metric metric : metrics){
            agg.putAll(metric.getMetricPart());
        }
        sentence.put("aggs",agg);
        return sentence;
    }

    /**
     * 获取全部分桶信息
     * @return
     */
    public List<Bucket> getBuckets() {
        return buckets;
    }
}
