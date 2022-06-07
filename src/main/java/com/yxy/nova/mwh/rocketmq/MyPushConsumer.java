package com.yxy.nova.mwh.rocketmq;

import com.ctg.mq.api.CTGMQFactory;
import com.ctg.mq.api.IMQPushConsumer;
import com.ctg.mq.api.PropertyKeyConst;
import com.ctg.mq.api.bean.MQResult;
import com.ctg.mq.api.listener.ConsumerTopicListener;
import com.ctg.mq.api.listener.ConsumerTopicStatus;
import lombok.SneakyThrows;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

/**
 * @author yuxiaoyu
 * @date 2022/6/6 上午10:31
 * @Description
 */
public class MyPushConsumer {

//    public static void main(String[] args) throws Exception {
//        final Properties properties = new Properties();
//        properties.setProperty(PropertyKeyConst.ConsumerGroupName, "test_consumer_1");
//        properties.setProperty(PropertyKeyConst.NamesrvAddr, "106.12.180.68:9876");
//        properties.setProperty(PropertyKeyConst.NamesrvAuthID, "rocketmq2");
//        properties.setProperty(PropertyKeyConst.NamesrvAuthPwd, "12345678");
//        properties.setProperty(PropertyKeyConst.ClusterName, "defaultMQBrokerCluster");
//        properties.setProperty(PropertyKeyConst.TenantID, "defaultMQTenantID");
//
//
//        IMQPushConsumer consumer = CTGMQFactory.createPushConsumer(properties);
//        int connectResult = consumer.connect();
//        if (connectResult != 0) {
//            return;
//        }
//        consumer.listenTopic("TopicTest", null, new ConsumerTopicListener() {
//            @SneakyThrows
//            @Override
//            public ConsumerTopicStatus onMessage(List<MQResult> mqResultList) {
//                //mqResultList 默认为1，可通过批量消费数量设置
//                for(MQResult result : mqResultList) {
//                    //TODO
////                    System.out.println(result);
//
//                    String s = new String(result.getMessage().getBody(), "utf-8");
//                    System.out.println(s);
//                }
//                return ConsumerTopicStatus.CONSUME_SUCCESS;//对消息批量确认(成功)
//                //return ConsumerTopicStatus.RECONSUME_LATER;//对消息批量确认(失败)
//            }
//        });
//    }



    public static void main(String[] args) throws InterruptedException, MQClientException {

        // 实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("please_rename_unique_group_name");

        // 设置NameServer的地址
        consumer.setNamesrvAddr("106.12.180.68:9876");

        // 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
        consumer.subscribe("TopicTest", "*");
        // 注册回调实现类来处理从broker拉取回来的消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
//                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                msgs.forEach(msg -> {
                    try {
                        String s = new String(msg.getBody(), "utf-8");
                        System.out.println(s);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                });
                // 标记该消息已经被成功消费
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        // 启动消费者实例
        consumer.start();
        System.out.printf("Consumer Started.%n");
    }

}
