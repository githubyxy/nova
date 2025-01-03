package test.rocketmq.normal;

/**
 * @author yxy
 * @description:  https://rocketmq.apache.org/zh/docs/sdk/02java  rocketmq官网 java示例
 *                  见包  org.apache.rocketmq:rocketmq-client-java
 *                 路径：org.apache.rocketmq.client.java.example;
 *
 * @date 2024/11/11 09:33
 */

import com.yxy.nova.mwh.utils.time.DateTimeUtil;
import lombok.SneakyThrows;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.rocketmq.YxyProducerSingleton;

import java.nio.charset.StandardCharsets;

public class ProducerNormalMessageExample {
    private static final Logger log = LoggerFactory.getLogger(ProducerNormalMessageExample.class);

    private ProducerNormalMessageExample() {
    }

    @SneakyThrows
    public static void main(String[] args) throws ClientException {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();

        // sh bin/mqadmin updatetopic -n localhost:9876 -t TestTopic -c DefaultCluster
        String topic = "sms-batch-send";
        final Producer producer = YxyProducerSingleton.getInstance(topic);

        while (true) {
            Thread.sleep(10000);

            // Define your message body.
            String msg = "测试rocketmq消息" + DateTimeUtil.datetime18();
            byte[] body = msg.getBytes(StandardCharsets.UTF_8);
//            String tag = Math.random() > 0.5D ? "tagA" : "tagB";
            String tag = "tag";
            final Message message = provider.newMessageBuilder()
                    // Set topic for the current message.
                    .setTopic(topic)
                    // Message secondary classifier of message besides topic.
                    .setTag(tag)
                    // Key(s) of the message, another way to mark message besides message id.
//                    .setKeys("yourMessageKey-1c151062f96e")
                    .setBody(body)
                    .build();

            try {
                final SendReceipt sendReceipt = producer.send(message);
                System.out.println("Send message successfully, tag=" + tag + ", msg=" + msg);
            } catch (Throwable t) {
                log.error("Failed to send message", t);
            }
            // Close the producer when you don't need it anymore.
            // You could close it manually or add this into the JVM shutdown hook.
            // producer.close();
        }


    }
}