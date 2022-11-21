package com.yxy.nova.mwh.kafka.multicast;

import com.yxy.nova.mwh.kafka.object.ProducerException;
import com.yxy.nova.mwh.kafka.object.QueueJob;
import com.yxy.nova.mwh.kafka.producer.SimpleProducer;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by xiazhen on 18/3/2.
 */
public class McastProducer extends SimpleProducer implements IMulticast {

    private static Charset UTF8 = Charset.forName("utf8");
    private static final int MAXSIZE = 524288;
    private String topic;
    // channel:The partition to which the record should be sent
    private int channel = -1;

    @Override
    public void init() throws IOException {
        super.init();
        super.setTopics(Lists.<String>newArrayList(topic));
        Preconditions.checkArgument(channel >= 0, "incorrect channel");
        if (!getConfigCenter().isNaive()) {
            //strong check if not naive
            Preconditions.checkArgument(getDefaultTopicConfig().isMulticast(), "topic is not marked with multicast");
        }
    }

    @Override
    public void multicast(final String message) throws ProducerException  {
        checkArg(
                message != null && message.getBytes(UTF8).length <=  MAXSIZE,
                "multicast message too long (max 524288 bytes)"
        );

        try {
            final Future<RecordMetadata> future = super.internalProduce(
                    new QueueJob(topic, UUID.randomUUID().toString(), message.getBytes(UTF8), System.currentTimeMillis()), channel, null
            );
            final RecordMetadata meta = future.get();
            Preconditions.checkArgument(meta != null, "meta is null");
        } catch (InterruptedException | ExecutionException e) {
            log.error("multicast error", e);
        }
    }

    @Override
    public List<String> getTopics() {
        return Lists.newArrayList(topic);
    }

    @Override
    public void setChannel(int channel) {
        this.channel = channel;
    }

    @Override
    public void setTopic(String topic) {
        this.topic = topic;
    }
}
