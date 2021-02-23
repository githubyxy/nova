package com.yxy.nova.mwh.elasticsearch.dto.agg;

import com.yxy.nova.mwh.elasticsearch.util.enumerate.MetricType;

import java.util.HashMap;
import java.util.Map;

/**
 * 计算结果
 * @author quyuanwen
 * @version 2020-03-02
 */
public class MetricResult {
    /**
     * 计算别名
     */
    private String name;

    /**
     * 计算类型
     */
    private MetricType metricType;
    /**
     * 默认值
     */
    private Object defaultValue;

    /**
     * 直接集合
     */
    private Map<MetricType, Object> metricValues;

    public MetricResult(String name, MetricType metricType,  Object defaultValue) {
        this.name = name;
        this.metricType = metricType;
        this.defaultValue = defaultValue;
    }

    /**
     * 获取默认值
     * @return
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    public Map<MetricType, Object> getMetricValues() {
        return metricValues;
    }

    public Object getMetricValue(MetricType metricType) {
        if(metricValues != null){
            return metricValues.get(metricType);
        }
        return defaultValue;
    }

    public void addMetricValue(MetricType type, Object value) {
        if(this.metricValues == null){
            this.metricValues = new HashMap<>();
        }
        this.metricValues.put(type, value);
    }

    public String getName() {
        return name;
    }

    public MetricType getMetricType() {
        return metricType;
    }

    @Override
    public String toString() {
        return "MetricResult{" + "name='" + name + '\'' + ", metricType=" + metricType + ", defaultValue=" + defaultValue + ", metricValues=" + metricValues + '}';
    }
}
