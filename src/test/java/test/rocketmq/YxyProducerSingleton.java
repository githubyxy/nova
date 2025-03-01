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

package test.rocketmq;

import org.apache.rocketmq.client.apis.*;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.ProducerBuilder;
import org.apache.rocketmq.client.apis.producer.TransactionChecker;

import java.time.Duration;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * Each client will establish an independent connection to the server node within a process.
 *
 * <p>In most cases, the singleton mode can meet the requirements of higher concurrency.
 * If multiple connections are desired, consider increasing the number of clients appropriately.
 */
public class YxyProducerSingleton {
    private static volatile Producer PRODUCER;
    private static volatile Producer TRANSACTIONAL_PRODUCER;
    private static final String ACCESS_KEY = "yourAccessKey";
    private static final String SECRET_KEY = "yourSecretKey";
//    private static final String ENDPOINTS = "127.0.0.1:8081";
    private static final String ENDPOINTS = "114.55.2.52:28081";


    private YxyProducerSingleton() {
    }

    private static Producer buildProducer(TransactionChecker checker, String... topics) throws ClientException {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();
        // Credential provider is optional for client configuration.
        // This parameter is necessary only when the server ACL is enabled. Otherwise,
        // it does not need to be set by default.
//        SessionCredentialsProvider sessionCredentialsProvider =
//            new StaticSessionCredentialsProvider(ACCESS_KEY, SECRET_KEY);
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
            .setEndpoints(ENDPOINTS)
            // On some Windows platforms, you may encounter SSL compatibility issues. Try turning off the SSL option in
            // client configuration to solve the problem please if SSL is not essential.
             .enableSsl(false)
//            .setCredentialProvider(sessionCredentialsProvider)
            .build();
        final ProducerBuilder builder = provider.newProducerBuilder()
            .setClientConfiguration(clientConfiguration)
            // Set the topic name(s), which is optional but recommended. It makes producer could prefetch
            // the topic route before message publishing.
            .setTopics(topics);
        if (checker != null) {
            // Set the transaction checker.
            builder.setTransactionChecker(checker);
        }
        return builder.build();
    }

    public static Producer getInstance(String... topics) throws ClientException {
        if (null == PRODUCER) {
            try {
                PRODUCER = buildProducer(null, topics);
            } catch (Exception e) {
                System.out.println("Failed to create producer: " + e.getMessage());
                throw e;
            }
        }
        return PRODUCER;
    }

    public static Producer getTransactionalInstance(TransactionChecker checker,
        String... topics) throws ClientException {
        if (null == TRANSACTIONAL_PRODUCER) {
            synchronized (YxyProducerSingleton.class) {
                if (null == TRANSACTIONAL_PRODUCER) {
                    TRANSACTIONAL_PRODUCER = buildProducer(checker, topics);
                }
            }
        }
        return TRANSACTIONAL_PRODUCER;
    }
}
