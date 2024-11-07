package test.kafka;

import com.yxy.nova.mwh.kafka.consumer.ConcurrentPoller;
import com.yxy.nova.mwh.kafka.consumer.IConsumer;
import com.yxy.nova.mwh.kafka.producer.SimpleProducer;
import com.yxy.nova.mwh.kafka.util.ZKConfigCenter;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yxy
 * @description:
 * @date 2024/11/7 16:54
 */
public class KafkaTest {

    @SneakyThrows
    @Test
    public void test() {
        ZKConfigCenter zkConfigCenter = new ZKConfigCenter();
        zkConfigCenter.setZkserver("127.0.0.1:2181");
        zkConfigCenter.setBusinessUnit("dev-common");
        zkConfigCenter.init();

        // 生产者
        SimpleProducer simpleProducer = new SimpleProducer();
        simpleProducer.setConfigCenter(zkConfigCenter);
        simpleProducer.setTopics(new ArrayList<>(Arrays.asList("sms_task_item","sms_task_item1")));
        simpleProducer.init();
        simpleProducer.produce("sms_task_item", "yxy", "yxy_name".getBytes("UTF-8"));

        // 消费者
        ConcurrentPoller concurrentPoller = new ConcurrentPoller();
        concurrentPoller.setConfigCenter(zkConfigCenter);
        concurrentPoller.setConsumerName("sms");
        concurrentPoller.setMode("standard");

        Map<String, IConsumer> bizConsumers = new HashMap<>();
        bizConsumers.put("sms_task_item", new ConsumerWithMonitor());

        concurrentPoller.setBizConsumers(bizConsumers);
        concurrentPoller.init();

        Thread.sleep(500000);

    }
}
