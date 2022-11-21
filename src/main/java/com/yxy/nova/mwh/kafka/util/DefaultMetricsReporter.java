package com.yxy.nova.mwh.kafka.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.metrics.KafkaMetric;
import org.apache.kafka.common.metrics.MetricsReporter;

import java.util.List;
import java.util.Map;

/**
 * Created by xiazhen on 18/6/27.
 */
public class DefaultMetricsReporter implements MetricsReporter {


    private static final List<Pair<String, String>> whitelist = Lists.newArrayList(
            Pair.of("producer-topic-metrics", "record-send-rate"),
            Pair.of("consumer-coordinator-metrics", "commit-latency-avg"),
            Pair.of("consumer-fetch-manager-metrics", "records-consumed-rate"),
            Pair.of("consumer-fetch-manager-metrics", "fetch-latency-avg")
    );

    private static Map<MetricName, KafkaMetric> data = Maps.newConcurrentMap();

    @Override
    public void init(List<KafkaMetric> lst) {
        for (final KafkaMetric km : lst) {
            metricChange(km);
        }
    }

    @Override
    public void metricChange(KafkaMetric metric) {
        final Pair<String, String> pair = Pair.of(
                metric.metricName().group(),
                metric.metricName().name()
        );

        if (pair.getRight().equalsIgnoreCase("records-consumed-rate")) {
            if (!metric.metricName().tags().containsKey("topic")) {
                return;
            }
        }

        if (whitelist.contains(pair)) {
            data.put(metric.metricName(), metric);
        }
    }

    @Override
    public void metricRemoval(KafkaMetric metric) {
        if (data.containsKey(metric.metricName())) {
            data.remove(metric.metricName());
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }

    public static Map<MetricName, KafkaMetric> getData() {
        return data;
    }
}
