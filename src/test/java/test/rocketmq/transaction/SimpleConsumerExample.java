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

package test.rocketmq.transaction;

import com.yxy.nova.mwh.utils.time.DateTimeUtil;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.SimpleConsumer;
import org.apache.rocketmq.client.apis.message.MessageId;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.shaded.org.slf4j.Logger;
import org.apache.rocketmq.shaded.org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

public class SimpleConsumerExample {
    private static final Logger log = LoggerFactory.getLogger(SimpleConsumerExample.class);

    private SimpleConsumerExample() {
    }

    @SuppressWarnings({"resource", "InfiniteLoopStatement"})
    public static void main(String[] args) throws ClientException {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();

        // Credential provider is optional for client configuration.
//        String accessKey = "yourAccessKey";
//        String secretKey = "yourSecretKey";
//        SessionCredentialsProvider sessionCredentialsProvider =
//            new StaticSessionCredentialsProvider(accessKey, secretKey);

        String endpoints = "127.0.0.1:8080";
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
            .setEndpoints(endpoints)
            // On some Windows platforms, you may encounter SSL compatibility issues. Try turning off the SSL option in
            // client configuration to solve the problem please if SSL is not essential.
            // .enableSsl(false)
//            .setCredentialProvider(sessionCredentialsProvider)
            .build();

        // ./bin/mqadmin updateSubGroup -c DefaultCluster -g yourFifoMessageGroup -n localhost:9876 -o true
        String consumerGroup = "yxyTransactionGroup";
        Duration awaitDuration = Duration.ofSeconds(60);
        String tag = "tagA";
        String topic = "TestTransactionTopic";
        FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);
        // In most case, you don't need to create too many consumers, singleton pattern is recommended.
        SimpleConsumer consumer = provider.newSimpleConsumerBuilder()
            .setClientConfiguration(clientConfiguration)
            // Set the consumer group name.
            .setConsumerGroup(consumerGroup)
            // set await duration for long-polling.
            .setAwaitDuration(awaitDuration)
            // Set the subscription for the consumer.
            .setSubscriptionExpressions(Collections.singletonMap(topic, filterExpression))
            .build();
        // Max message num for each long polling.
        int maxMessageNum = 16;
        // Set message invisible duration after it is received.
        Duration invisibleDuration = Duration.ofSeconds(13);
        // Receive message, multi-threading is more recommended.
        do {
            final List<MessageView> messages = consumer.receive(maxMessageNum, invisibleDuration);
//            System.out.println("Received {} message(s)" + messages.size());
            for (MessageView message : messages) {
                try {
                    String body = StandardCharsets.UTF_8.decode(message.getBody()).toString();
                    System.out.println(DateTimeUtil.datetime18() + " Received message: " + body + ", tag" + message.getTag().get());
//                    if (body.endsWith("22")) {
//                        System.out.println("模拟失败:" + body);
//                        System.out.println(1/0);
//                    }
                    MessageId messageId = message.getMessageId();
                    consumer.ack(message);
//                    System.out.println("Message is acknowledged successfully, messageId={}" + messageId);
                } catch (Throwable t) {
                    System.out.println("Message is failed to be acknowledged, t={}" + t.getMessage());
                }
            }
        } while (true);
        // Close the simple consumer when you don't need it anymore.
        // You could close it manually or add this into the JVM shutdown hook.
        // consumer.close();
    }
}
