package com.yxy.nova.mwh.kafka.util;

import com.yxy.nova.mwh.kafka.object.ClusterConfig;
import com.yxy.nova.mwh.kafka.object.ConsumerConfig;
import com.yxy.nova.mwh.kafka.object.TopicConfig;

import java.io.Closeable;

/**
 * Created by xiazhen on 18/6/26.
 */
public interface IConfigCenter extends Closeable {

    /**
     * 当前运行环境是否是naive模式（由当前所连接的注册中心决定）
     */
    boolean isNaive();

    String queryInfluxdb();

    ClusterConfig queryCluster(String cluster);

    /**
     * 查询这个topic的注册配置，若当前为naive模式，则使用默认配置
     */
    TopicConfig queryTopic(String topic);

    /**
     * 查询这个consumer的注册配置，若当前为naive模式，则使用默认配置
     */
    ConsumerConfig queryConsumer(final String name);

}
