package com.yxy.nova.mwh.eunomia.client.consumer.kafka;

import com.yxy.nova.mwh.eunomia.client.consumer.EunomiaConsumer;
import com.yxy.nova.mwh.eunomia.client.exception.EunomiaClientException;
import com.yxy.nova.mwh.eunomia.client.listener.EunomiaListener;
import com.yxy.nova.mwh.eunomia.client.message.RowData;
import com.yxy.nova.mwh.kafka.consumer.IConsumer;
import com.yxy.nova.mwh.kafka.object.RetryLaterException;
import com.yxy.nova.mwh.kafka.util.IConfigCenter;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by toruneko on 2016/12/7.
 */
public class EunomiaKafkaConsumer extends AbstractKafkaConsumer implements EunomiaConsumer, IConsumer {

    protected static final Logger logger = LoggerFactory.getLogger(EunomiaKafkaConsumer.class);

    private EunomiaListener listener;

    public EunomiaKafkaConsumer(String topic, EunomiaListener listener, IConfigCenter configCenter) {
        super(topic, configCenter);
        this.listener = listener;
    }

    @Override
    protected IConsumer getConsumer() {
        return this;
    }

    @Override
    public void doConsume(List<ConsumerRecord<String, byte[]>> messages) {
        for (ConsumerRecord<String, byte[]> message : messages) {
            try {
                if (!onMessage(message.value())) {
                    throw new RetryLaterException();
                }
            } catch (EunomiaClientException e) {
                logger.warn(e.getMessage(), e.getCause());
                throw new RetryLaterException();
            }
        }
    }

    @Override
    public boolean onMessage(byte[] message) {
        if (message == null || message.length == 0) {
            return true; // 空的消息，跳过
        }

        RowData rowData;
        try {
            rowData = JSON.parseObject(new String(message, Charsets.UTF_8), RowData.class);
        } catch (Exception e) {
            //垃圾消息，ack掉
            return true;
        }

        try {
            return listener.onEvent(rowData);
        } catch (Exception e) {
            throw new EunomiaClientException("process kafka message error", e);
        }
    }
}
