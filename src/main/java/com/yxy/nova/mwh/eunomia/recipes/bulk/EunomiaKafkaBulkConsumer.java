package com.yxy.nova.mwh.eunomia.recipes.bulk;

import com.yxy.nova.mwh.eunomia.client.consumer.kafka.EunomiaKafkaConsumer;
import com.yxy.nova.mwh.eunomia.client.listener.EunomiaListener;
import com.yxy.nova.mwh.eunomia.client.message.RowData;
import com.yxy.nova.mwh.kafka.object.RetryLaterException;
import com.yxy.nova.mwh.kafka.util.IConfigCenter;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toruneko on 2017/4/18.
 */
public class EunomiaKafkaBulkConsumer extends EunomiaKafkaConsumer {

    private static final EunomiaThreadLocalListener magicListener = new EunomiaThreadLocalListener();

    private EunomiaBulkListener listener;

    public EunomiaKafkaBulkConsumer(String topic, EunomiaBulkListener listener, IConfigCenter configCenter) {
        super(topic, magicListener, configCenter);
        this.listener = listener;
    }

    @Override
    public void doConsume(List<ConsumerRecord<String, byte[]>> messages) {
        List<RowData> rowDataList = new ArrayList<>(messages.size());

        for (ConsumerRecord<String, byte[]> message : messages) {
            if (!onMessage(message.value())) {
                RowData rowData = magicListener.getRowData();
                if (rowData == null) {
                    continue;
                }
                rowDataList.add(rowData);
            }
        }

        try {
            listener.onEvent(rowDataList);
        } catch (Exception e) {
            logger.warn("process kafka message error", e);
            throw new RetryLaterException();
        }
    }

    static class EunomiaThreadLocalListener implements EunomiaListener {

        private ThreadLocal<RowData> rowData = new ThreadLocal<>();

        @Override
        public boolean onEvent(RowData rowData) throws Exception {
            this.rowData.set(rowData);
            return false;
        }

        public RowData getRowData() {
            return rowData.get();
        }
    }
}
