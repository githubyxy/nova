package com.yxy.nova.mwh.kafka.util;

import com.yxy.nova.mwh.kafka.object.ClusterConfig;
import com.yxy.nova.mwh.kafka.object.ConsumerConfig;
import com.yxy.nova.mwh.kafka.object.SubscriberConfig;
import com.yxy.nova.mwh.kafka.object.TopicConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;

/**
 * 仅供线下开发测试使用
 *
 * Created by xiazhen on 18/6/27.
 */
public class MockConfigCenter implements IConfigCenter {

    public static final String TOPIC = "angelia_sms_reply_yqgj";//"perf-test";
    public static final String COMPLEX_TOPIC = String.format("%s,%s2", TOPIC, TOPIC);

    public static final ObjectMapper mapper = new ObjectMapper();

    private void print(Object value) {
        try {
            System.out.println(mapper.writeValueAsString(value));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isNaive() {
        return false;
    }

    @Override
    public String queryInfluxdb() {
        return null;
    }

    @Override
    public TopicConfig queryTopic(String topic) {
        TopicConfig tc = new TopicConfig();
        tc.setAcks(-1);
        tc.setClusterName("dev");
        tc.setLinger(5);
        tc.setGmtCreated(System.currentTimeMillis());
        tc.setTopic(topic);
        tc.setRetries(0);
        tc.setMaxSize(1024 * 500);
        print (tc);
        return tc;
    }

    @Override
    public ClusterConfig queryCluster(String cluster) {
        ClusterConfig cc = new ClusterConfig();
        cc.setName(cluster);
        cc.setDc("hz");
        cc.setBrokers("192.168.6.52:9192,192.168.6.53:9192");
        cc.setZookeeper("192.168.6.55:2181,192.168.6.56:2181,192.168.6.57:2181/newkafka");
        print(cc);
        return cc;
    }

    @Override
    public ConsumerConfig queryConsumer(String name) {
        ConsumerConfig cc = new ConsumerConfig();
        SubscriberConfig ctc = new SubscriberConfig();
        cc.setName(MockConfigCenter.TOPIC + "-consumer");
        cc.setSubscribed(Collections.singletonMap(MockConfigCenter.TOPIC, ctc));
        print(cc);
        return cc;
    }

    @Override
    public void close() throws IOException {

    }
}
