package com.yxy.nova.mwh.rocketmq;

import com.ctg.mq.api.CTGMQFactory;
import com.ctg.mq.api.IMQProducer;
import com.ctg.mq.api.PropertyKeyConst;
import com.ctg.mq.api.bean.MQMessage;
import com.ctg.mq.api.bean.MQSendResult;
import com.ctg.mq.api.exception.MQException;
import com.ctg.mq.api.exception.MQProducerException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * @author yuxiaoyu
 * @date 2022/6/6 上午10:24
 * @Description
 */
public class MyProducer {

//    @PostConstruct
//    public static void main(String[] args) throws InterruptedException, MQException {
//        Properties properties = new Properties();
//        properties.setProperty(PropertyKeyConst.ProducerGroupName, "producer_group_yxy");
//        properties.setProperty(PropertyKeyConst.NamesrvAddr, "106.12.180.68:9876");
//        properties.setProperty(PropertyKeyConst.NamesrvAuthID, "rocketmq2");
//        properties.setProperty(PropertyKeyConst.NamesrvAuthPwd, "12345678");
//        properties.setProperty(PropertyKeyConst.ClusterName, "DefaultCluster");
//        properties.setProperty(PropertyKeyConst.TenantID, "defaultMQTenantID");
//
//        IMQProducer producer = CTGMQFactory.createProducer(properties);//建议应用启动时创建
//        int connectResult = producer.connect();
//        if(connectResult != 0){
//            System.out.println(connectResult);
//            return;
//        }
//        for (int i = 0; i < 10; i++) {
//            try {
//                MQMessage message = new MQMessage(
//                        "TopicTest",// topic
//                        "ORDER_KEY_"+i,// key
//                        "ORDER_TAG",//tag
//                        ("HELLO ORDER BODY" + i).getBytes(RemotingHelper.DEFAULT_CHARSET)// body
//                );
//                MQSendResult sendResult = producer.send(message);
//                System.out.println(sendResult);
//                //TODO
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//        }
//        producer.close();//建议应用关闭时关闭
//    }



        public static void main(String[] args) throws Exception {
            // 实例化消息生产者Producer
            DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name", null);
            // 设置NameServer的地址
            producer.setNamesrvAddr("106.12.180.68:9876");
//            producer.setSendMsgTimeout(60000);
//            producer.setVipChannelEnabled(false);
            // 启动Producer实例
            producer.start();
            for (int i = 0; i < 10; i++) {
                // 创建消息，并指定Topic，Tag和消息体
                Message msg = new Message("TopicTest" /* Topic */,
                        "TagA" /* Tag */,
                        ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
                );
                // 发送消息到一个Broker
                SendResult sendResult = producer.send(msg);
                // 通过sendResult返回消息是否成功送达
                System.out.printf("%s%n", sendResult);
            }
            // 如果不再发送消息，关闭Producer实例。
            producer.shutdown();
        }

}
