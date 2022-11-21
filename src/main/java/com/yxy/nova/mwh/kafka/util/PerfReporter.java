package com.yxy.nova.mwh.kafka.util;

import com.google.common.collect.Sets;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.metrics.KafkaMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

/**
 * it's not well designed but works
 *
 * Created by xiazhen on 18/6/27.
 */
public class PerfReporter {

    private static final Logger log = LoggerFactory.getLogger("module-kafka");
    private static final CloseableHttpClient httpClient;
    static {
        final RequestConfig.Builder requestBuilder = RequestConfig.custom()
                .setSocketTimeout(3000)
                .setConnectTimeout(3000)
                .setConnectionRequestTimeout(3000);
        httpClient = HttpClientBuilder
                .create()
                .setDefaultRequestConfig(requestBuilder.build())
                .build();
    }

    private static final Timer reporter = new Timer();

//    private static String influxdb = null;
    private static Set<ReportBuilder> builders = Sets.newHashSet();
    private static String hostname;

    private static abstract class ReportBuilder {
        protected final String type;
        protected ReportBuilder(String type) {
            this.type = type;
        }
        abstract public void build(final List<String> data);
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ReportBuilder)) return false;
            ReportBuilder that = (ReportBuilder) o;
            return type.equals(that.type);

        }
        @Override
        public int hashCode() {
            return type.hashCode();
        }
    }

    static {
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException("unable to initiate due to incorrect host binding !!!");
        }

//        reporter.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (influxdb == null) return;
//                final List<String> reportData = Lists.newArrayList();
//                for (final ReportBuilder rb : builders) {
//                    rb.build(reportData);
//                }
//                if (reportData.size() == 0) return;
//                final HttpPost post = new HttpPost(influxdb);
//                CloseableHttpResponse resp = null;
//                try {
//                    post.setEntity(new StringEntity(StringUtils.join(reportData, '\n')));
//                    resp = httpClient.execute(post);
//                    log.debug("reporter looped once with resp status {}", resp.getStatusLine().getStatusCode());
//                } catch (Exception e) {
//                    log.error("reported loop failed:", e);
//                } finally {
//                    post.releaseConnection();
//                    if (resp != null) {
//                        try {
//                            resp.close();
//                        } catch (IOException e) {
//                        }
//                    }
//                }
//            }
//        }, 30 * 1000, 20 * 1000);
    }

    private static class ConsumerReportBuilder extends ReportBuilder {

        protected ConsumerReportBuilder(String type) {
            super(type);
        }

        public void build(final List<String> data) {
            //"measurement_name,tag1=value1,tag2=value2 field1=value1,field2=value2"
            final Map<MetricName, KafkaMetric> metrics = DefaultMetricsReporter.getData();
            for (final MetricName mn : metrics.keySet()) {
                if (mn.group().startsWith("consumer-")) {
                    final StringBuilder sb = new StringBuilder(type).append(',');
                    if (mn.tags().containsKey("topic")) {
                        sb.append("topic=").append(mn.tags().get("topic")).append(',');
                    }
                    sb.append("host=").append(hostname).append(' ');
                    sb.append(mn.name()).append('=').append(metrics.get(mn).metricValue());
                    data.add(sb.toString());
                }
            }
        }
    }

    private static class ProducerReportBuilder extends ReportBuilder {
        protected ProducerReportBuilder(String type) {
            super(type);
        }

        @Override
        public void build(List<String> data) {
            final Map<MetricName, KafkaMetric> metrics = DefaultMetricsReporter.getData();
            for (final MetricName mn : metrics.keySet()) {
                if (mn.group().startsWith("producer-")) {
                    final StringBuilder sb = new StringBuilder("t-").append(mn.tags().get("topic")).append(',');
                    sb.append("host=").append(hostname).append(' ');
                    sb.append(mn.name()).append('=').append(metrics.get(mn).metricValue());
                    data.add(sb.toString());
                }
            }
        }
    }

    public static synchronized void init(final String influxdb, final String type) {
//        if (StringUtils.isEmpty(influxdb)) {
//            log.error("PerfReporter init with empty influxdb endpoint");
//            return;
//        }
//        if (PerfReporter.influxdb == null) {
//            PerfReporter.influxdb = influxdb;
//        }

        if (type.startsWith("p")) {
            builders.add(new ProducerReportBuilder(type));
        } else if (type.startsWith("c")) {
            builders.add(new ConsumerReportBuilder(type));
        }
    }

    public static void close() {
        reporter.cancel();
        reporter.purge();
    }
}
