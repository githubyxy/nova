package com.yxy.nova.mwh.kafka.multicast;

import com.yxy.nova.mwh.kafka.consumer.IConsumer;
import com.yxy.nova.mwh.kafka.consumer.Mode;
import com.yxy.nova.mwh.kafka.consumer.StandardPoller;
import com.yxy.nova.mwh.kafka.object.ClusterConfig;
import com.yxy.nova.mwh.kafka.object.TopicConfig;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 基于Kafka的可靠广播
 *
 * Created by xiazhen on 18/3/1.
 */
public class McastPoller extends StandardPoller {

    private final String zkPath = "/kafka-mcast/offset/%s/%s";

    private String topic;
    private int channel = -1;
    private IConsumer bizConsumer;

    private String host;
    private CuratorFramework cf;
    private long offset = -1;

    @Override
    protected void commit(
            KafkaConsumer<String, byte[]> consumer,
            Map<TopicPartition, OffsetAndMetadata> offsets) {
        //广播模式下, 一个消息者只能assign一个partition
        Preconditions.checkArgument(offsets.size() == 1, "multicast commit failed due to multiple partitions");
        final TopicPartition tp = new TopicPartition(topic, channel);
        final OffsetAndMetadata om = offsets.get(tp);
        Preconditions.checkArgument(om != null, "offset not found for topic {}", topic);
        this.offset = om.offset();
        final String path = String.format(zkPath, topic, host);
        try {
            cf.setData().forPath(path, String.valueOf(offset).getBytes("UTF-8"));
            log.warn("McastPoller commit offset {} for {}", offset, topic);
        } catch (Exception e) {
            log.error("failed to update mcast offset {} for topic {}", offset, topic);
        }
    }

    @Override
    protected void onPollingWorkerStarted(KafkaConsumer<String, byte[]> consumer) {
        final TopicPartition tp = new TopicPartition(topic, channel);
        if (offset < 0) {
            consumer.seekToEnd(Lists.newArrayList(tp));
        } else {
            consumer.seek(tp, offset);
        }
    }

    @Override
    public void init() throws Exception {
        final List<Integer> pts = Lists.newArrayList(channel);
        setPartitions(Collections.singletonMap(topic, pts));
        setBizConsumers(Collections.singletonMap(topic, bizConsumer));
        setMode(Mode.PARANOID.name());//that's where magic starts

        this.host = InetAddress.getLocalHost().getHostName();
        final TopicConfig tc = getConfigCenter().queryTopic(topic);
        final ClusterConfig clusterConfig = getConfigCenter().queryCluster(tc.getClusterName());
        cf = CuratorFrameworkFactory.builder().connectString(clusterConfig.getZookeeper())
                .sessionTimeoutMs(10 * 1000)
                .retryPolicy(new ExponentialBackoffRetry(100, 10, 5000))
                .build();
        cf.start();
        cf.blockUntilConnected();
        log.warn("McastPoller started successfully");
        final String path = String.format(zkPath, topic, host);
        final Stat stat = cf.checkExists().forPath(path);
        if (stat == null) {
            cf.create().creatingParentsIfNeeded().forPath(path, String.valueOf(-1).getBytes("UTF-8"));
            log.warn("McastPoller for {} created and seek to latest", host);
        } else {
            final byte[] bb = cf.getData().forPath(path);
            offset = Long.parseLong(new String(bb, "UTF-8"));
            log.warn("McastPoller for {} started with last offset {} loaded", host, offset);
        }
        super.init();
    }

    @Override
    public void close() throws IOException {
        super.close();
        cf.close();
        log.warn("McastPoller for {} closed", topic);
    }


    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public void setBizConsumer(IConsumer bizConsumer) {
        this.bizConsumer = bizConsumer;
    }
}
