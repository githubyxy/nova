package com.yxy.nova.mwh.kafka.util;

import com.yxy.nova.mwh.kafka.consumer.IConsumer;
import com.yxy.nova.mwh.kafka.multicast.IMulticast;
import com.yxy.nova.mwh.kafka.multicast.McastPoller;
import com.yxy.nova.mwh.kafka.multicast.McastProducer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xiazhen on 18/7/2.
 */
public class McastAppTest {

    private static final String TOPIC = "mcast-test";
    private static final int CHANNEL = 3;
    private static final int TOTAL = 10;
    private static final Logger log = LoggerFactory.getLogger("module-kafka");

    private static final AtomicInteger received = new AtomicInteger(0);

    IMulticast multicast;
    McastPoller poller;
    ZKConfigCenter zcc;

    private void setup() throws Exception {

        zcc = new ZKConfigCenter();
        zcc.setZkserver("192.168.6.55:2181,192.168.6.56:2181,192.168.6.57:2181");
        zcc.setBusinessUnit("main");
        zcc.init();

        McastProducer producer = new McastProducer();
        producer.setChannel(CHANNEL);
        producer.setTopic(TOPIC);
        producer.setConfigCenter(zcc);
        producer.init();
        this.multicast = producer;

        poller = new McastPoller();
        poller.setConfigCenter(zcc);
        poller.setConsumerName("perf-test-consumer");
        poller.setTopic(TOPIC);
        poller.setChannel(CHANNEL);
        poller.setBizConsumer(new MyConsumer());
        poller.init();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    multicast.close();
                    poller.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void start() {
        new Thread("sender") {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < TOTAL; i++) {
                        multicast.multicast(String.valueOf(System.currentTimeMillis()));
                    }
                    Thread.sleep(5000);
                    multicast.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    static class MyConsumer implements IConsumer {
        @Override
        public void doConsume(List<ConsumerRecord<String, byte[]>> messages) {
            for (ConsumerRecord<String, byte[]> rec : messages) {
                try {
                    final byte[] val = rec.value();
                    log.warn("received:{}:{}", rec.offset(), new String(val, "UTF-8"));
                    received.incrementAndGet();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] argv) throws Exception {
        McastAppTest app = new McastAppTest();
        app.setup();
        app.start();
        while (true) {
            Thread.sleep(1000);
            if (received.get() == TOTAL) {
                System.out.println("all mcast messages received!");
                app.poller.close();
                app.zcc.close();
                break;
            }
        }
    }
}
