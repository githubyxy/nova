package com.yxy.nova.mwh.kafka.util;

import com.yxy.nova.mwh.kafka.consumer.StandardPoller;
import com.yxy.nova.mwh.kafka.object.ComplexTopic;
import com.yxy.nova.mwh.kafka.producer.IProducer;
import com.yxy.nova.mwh.kafka.producer.SimpleProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * 用于测试一些非正常场景
 *
 * 1,由于某种原因发生leader election
 *
 */
public class SpecialTest {

    protected static final Logger log = LoggerFactory.getLogger("module-kafka");
    private IConfigCenter configCenter;
    private IProducer producer;
    private StandardPoller poller;

    public static void main(String[] argv) throws Exception {
        log.warn("SpecialTest started!!!");
        SpecialTest st = new SpecialTest();
        st.prepare();
        st.case1();
        st.close();
    }

    public void close() throws Exception {
        configCenter.close();
        if (producer != null) producer.close();
        if (poller != null) poller.close();
    }

    public void prepare() throws Exception {
        final ZKConfigCenter zcc = new ZKConfigCenter();
        zcc.setZkserver("192.168.6.55:2181,192.168.6.56:2181,192.168.6.57:2181");
        zcc.setBusinessUnit("main");
        zcc.init();
        this.configCenter = zcc;

        final SimpleProducer sp = new SimpleProducer();
        final ComplexTopic complexTopic = ComplexTopic.create(MockConfigCenter.COMPLEX_TOPIC);
        sp.setConfigCenter(configCenter);
        sp.setTopics(complexTopic.getParts());
        sp.setPersistOnError(true);
        sp.setPersistenceFile("/tmp/module-kafka-test.bin");
        sp.init();
        producer = sp;
    }

    public void case1() throws Exception {
        boolean ret = false;
        for (int i = 0; i < 100; i++) {
            try {
                ret = producer.syncProduce(
                        MockConfigCenter.TOPIC,
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString().getBytes("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.printf("sent@%d:%s\n",i, ret);
            //在循环结束前在命令行进行模拟重新选举，这里就会触发异常
            Thread.sleep(200);
        }
    }
}
