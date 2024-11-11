package test.rocketmq.normal;

/**
 * @author yxy
 * @description:  https://rocketmq.apache.org/zh/docs/sdk/02java  rocketmq官网 java示例
 *                  见包  org.apache.rocketmq:rocketmq-client-java
 *                 路径：org.apache.rocketmq.client.java.example;
 *
 * @date 2024/11/11 09:33
 */
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

        String topic = "TestTopic";
        final Producer producer = YxyProducerSingleton.getInstance(topic);

        while (true) {
            Thread.sleep(10000);

            // Define your message body.
            String msg = "测试rocketmq消息" + DateTimeUtil.datetime18();
            byte[] body = msg.getBytes(StandardCharsets.UTF_8);
//            String tag = Math.random() > 0.5D ? "tagA" : "tagB";
            String tag = "tagB";
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