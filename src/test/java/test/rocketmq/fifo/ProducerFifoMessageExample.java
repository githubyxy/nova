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

package test.rocketmq.fifo;

import com.yxy.nova.mwh.utils.time.DateTimeUtil;
import lombok.SneakyThrows;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.shaded.org.slf4j.Logger;
import org.apache.rocketmq.shaded.org.slf4j.LoggerFactory;
import test.rocketmq.YxyProducerSingleton;

import java.nio.charset.StandardCharsets;

public class ProducerFifoMessageExample {
    private static final Logger log = LoggerFactory.getLogger(ProducerFifoMessageExample.class);

    private ProducerFifoMessageExample() {
    }

    @SneakyThrows
    public static void main(String[] args) throws ClientException {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();

        // sh mqadmin updateTopic -n localhost:9876 -t TestFifoTopic -c DefaultCluster -a +message.type=FIFO
        String topic = "TestFifoTopic";
        final Producer producer = YxyProducerSingleton.getInstance(topic);

        while (true) {
            Thread.sleep(10000);

            // Define your message body.
            String msg = "测试rocketmq FIFO消息" + DateTimeUtil.datetime18();
            byte[] body = msg.getBytes(StandardCharsets.UTF_8);
            String tag = "tagA";
            final Message message = provider.newMessageBuilder()
                    // Set topic for the current message.
                    .setTopic(topic)
                    // Message secondary classifier of message besides topic.
                    .setTag(tag)
                    // Key(s) of the message, another way to mark message besides message id.
//            .setKeys("yourMessageKey-1ff69ada8e0e")
                    // Message group decides the message delivery order.
                    .setMessageGroup("yourFifoMessageGroup")
                    .setBody(body)
                    .build();
            try {
                final SendReceipt sendReceipt = producer.send(message);
                System.out.println("Send message successfully, tag=" + tag + ", msg=" + msg);
            } catch (Throwable t) {
                log.error("Failed to send message", t);
            }
        }

        // Close the producer when you don't need it anymore.
        // You could close it manually or add this into the JVM shutdown hook.
        // producer.close();
    }
}
