package com.yxy.nova.mwh.elasticsearch.basic.agg.bucket;

import com.yxy.nova.mwh.elasticsearch.basic.agg.metrics.Metric;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 桶
 * @author quyuanwen
 * @version 2020-02-28
 */
public abstract class Bucket {

    private String name;

    private String field;

    private List<Metric> metrics;

    private List<Bucket>  subs;

    public Bucket(String name, String field) {
        this.name = name;
        this.field = field;
    }

    /**
     * 获取聚合语句
     * @return
     */
    public JSONObject getBucketPart(){
        JSONObject agg = new JSONObject();
        JSONObject content = assemble(this.field);
        if(content != null && !content.isEmpty()){
            agg.put(name, content);
            JSONObject subAgg = null;
            //封装子桶
            if(!CollectionUtils.isEmpty(subs)){
                subAgg = new JSONObject();
                for(int i = 0; i < subs.size(); i++){
                    subAgg.putAll(subs.get(i).getBucketPart());
                }
            }
            //封装计算指标
            if(!CollectionUtils.isEmpty(metrics)){
                if(subAgg == null){
                    subAgg = new JSONObject();
                }
                for(int i = 0; i < metrics.size(); i++){
                    subAgg.putAll(metrics.get(i).getMetricPart());
                }
            }
            if(subAgg != null){
                content.put("aggs", subAgg);
            }
        }
        return agg;
    }

    /**
     * 组装语句
     * @return
     */
    public abstract JSONObject assemble(String field);

    public List<Metric> getMetrics() {
        return metrics;
    }

    public Bucket setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
        return this;
    }

    public Bucket metric(Metric metric) {
        if(CollectionUtils.isEmpty(this.metrics)){
            this.metrics = new ArrayList<>();
        }
        this.metrics.add(metric);
        return this;
    }

    public List<Bucket> getSubs() {
        return this.subs;
    }

    public Bucket sub(Bucket sub) {
        if(CollectionUtils.isEmpty(this.subs)){
            this.subs = new ArrayList<>();
        }
        this.subs.add(sub);
        return this;
    }

    public String getName() {
        return name;
    }

    public String getField() {
        return field;
    }
}
