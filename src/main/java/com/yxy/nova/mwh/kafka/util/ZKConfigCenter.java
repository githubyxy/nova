package com.yxy.nova.mwh.kafka.util;

import com.yxy.nova.mwh.kafka.object.ClusterConfig;
import com.yxy.nova.mwh.kafka.object.ConsumerConfig;
import com.yxy.nova.mwh.kafka.object.TopicConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 *
 * Created by xiazhen on 18/7/2.
 */
public class ZKConfigCenter implements IConfigCenter {

    private static final Logger log = LoggerFactory.getLogger("module-kafka");
    private static final String ROOT = "/kafka-mq";

    private ObjectMapper mapper = new ObjectMapper();
    private Cache<String, Object> clusterCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES).build();
    private CuratorFramework curatorClient;
    private long touch = 0;
    private Timer timer = new Timer();

    private String businessUnit;
    private String zkserver;
    private int sessionTimeout = 15000;

    public void init() throws Exception {
        Preconditions.checkArgument(businessUnit != null, "incorrect businessUnit");
        Preconditions.checkArgument(StringUtils.isNotEmpty(zkserver), "incorrect zk server");
        final Stat stat = obtain().checkExists().forPath("/");
        Preconditions.checkArgument(stat != null, "businessUnit not exists:" + businessUnit);
        // 获取zk下 /kafka-mq/{default.business.unit} 节点的值
        final byte[] bb = obtain().getData().forPath("/");
        globalConfig = mapper.readValue(bb, HashMap.class);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (curatorClient == null) return;
                synchronized(ZKConfigCenter.class) {
                    if (System.currentTimeMillis() - touch > 5 * 60 * 1000) {
                        log.warn("ZKConfigCenter haven't been used for 5 minutes and will be closed soon");
                        curatorClient.close();
                        curatorClient = null;
                    }
                }
            }
        }, 10 * 1000, 60 * 1000);
    }

    private CuratorFramework obtain() throws Exception {
        synchronized(ZKConfigCenter.class) {
            touch = System.currentTimeMillis();
            if (curatorClient != null) {
                return curatorClient;
            }
            log.warn("ZKConfigCenter started successfully");
            curatorClient = CuratorFrameworkFactory.builder().connectString(zkserver + ROOT + "/" + businessUnit)
                    .sessionTimeoutMs(sessionTimeout)
                    .retryPolicy(new ExponentialBackoffRetry(100, 10, 5000))
                    .build();
            curatorClient.start();
            curatorClient.blockUntilConnected();
            return curatorClient;
        }
    }

    private <T> T query(Class<T> cls, String type, String obj) throws Exception {
        final StringBuilder sb = new StringBuilder();
        sb.append('/');
        sb.append(type);
        final String defaultPath = sb.toString();
        //if obj is 'default' then use the default config
        if (StringUtils.isNotEmpty(obj) && !"default".equalsIgnoreCase(obj)) {
            sb.append('/').append(obj);
        }
        String path = sb.toString();
        Stat stat = obtain().checkExists().forPath(path);
        if (isNaive() && stat == null) {
            // if naive mode then fall back to default config
            stat = obtain().checkExists().forPath(defaultPath);
            path = defaultPath;
        }
        Preconditions.checkArgument(stat != null && stat.getCtime() > 0, "config not found:" + type + ":" + obj);
        final byte[] bb = obtain().getData().forPath(path);
        return mapper.readValue(bb, cls);
    }

    private HashMap<String, Object> globalConfig = null;

    @Override
    public boolean isNaive() {
        return globalConfig != null &&
                globalConfig.containsKey("naive") &&
                (boolean)globalConfig.get("naive");
    }

    @Override
    public String queryInfluxdb() {
        try {
            return (String) globalConfig.get("influxdb");
        } catch (Exception e) {
            log.error("queryInfluxdb failed", e);
        }
        return null;
    }

    //strip topic to its original format if it's synced!
    private static String stripTopic(final String topic) {
        if (topic.endsWith("2")) {
            return topic.substring(0, topic.length() - 1);
        } else {
            return topic;
        }
    }

    public static void main(String[] argv) throws Exception {
        final ZKConfigCenter zcc = new ZKConfigCenter();
        zcc.setBusinessUnit("dev");
        zcc.setZkserver("106.13.148.83:2181");
        try {
            zcc.init();
            final TopicConfig tc = zcc.queryTopic("perf-test");
            System.out.println(StringUtils.join(tc.getOwners(), ","));
            final ConsumerConfig cc = zcc.queryConsumer("perf-test-consumer");//this does exist
            System.out.println(cc.getOne("perf-test", true).get("fetch.max.wait.ms"));

            final ConsumerConfig cc1 = zcc.queryConsumer("perf-test-consumer-XXX");//this does not exist
            System.out.println(cc1.getOne("perf-test", true).get("fetch.max.wait.ms"));
            System.out.println("we are good");
        } finally {
            zcc.close();
        }

    }

    @Override
    public TopicConfig queryTopic(String topic) {
        try {
            final String obj = stripTopic(topic);
            final TopicConfig tc = (TopicConfig) clusterCache.get("TOPIC-" + topic, new Callable<TopicConfig>() {
                @Override
                public TopicConfig call() throws Exception {
                    return query(TopicConfig.class, "topic", obj);
                }
            });
            tc.setTopic(topic);
            return tc;
        } catch (ExecutionException e) {
            log.error("queryTopic failed", e);
        }
        return null;
    }

    @Override
    public ClusterConfig queryCluster(final String cluster) {
        try {
            return (ClusterConfig) clusterCache.get("CLUSTER-" + cluster, new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return query(ClusterConfig.class, "cluster", cluster);
                }
            });
        } catch (ExecutionException e) {
            log.error("queryCluster failed", e);
        }
        return null;
    }

    @Override
    public ConsumerConfig queryConsumer(final String name) {
        try {
            final ConsumerConfig cc = (ConsumerConfig) clusterCache.get("CONSUMER-" + name, new Callable<ConsumerConfig>() {
                @Override
                public ConsumerConfig call() throws Exception {
                    return query(ConsumerConfig.class, "consumer", name);
                }
            });
            cc.setName(name);
            return cc;
        } catch (ExecutionException e) {
            log.error("queryConsumer failed", e);
        }
        return null;
    }

    public void setZkserver(String zkserver) {
        this.zkserver = zkserver;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    @Override
    public void close() throws IOException {
        timer.cancel();
        if (curatorClient != null) {
            curatorClient.close();
        }
    }
}
