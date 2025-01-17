package test.rocketmq.normal;

import com.yxy.nova.mwh.utils.time.DateTimeUtil;
import org.apache.rocketmq.client.apis.*;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class PushConsumerExample {
    private static final Logger log = LoggerFactory.getLogger(PushConsumerExample.class);

    private PushConsumerExample() {
    }

    public static void main(String[] args) throws ClientException, InterruptedException, IOException {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();

        // Credential provider is optional for client configuration.
        String accessKey = "yourAccessKey";
        String secretKey = "yourSecretKey";
        SessionCredentialsProvider sessionCredentialsProvider =
                new StaticSessionCredentialsProvider(accessKey, secretKey);

        String endpoints = "114.55.2.52:28081";
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
                .setEndpoints(endpoints)
                // On some Windows platforms, you may encounter SSL compatibility issues. Try turning off the SSL option in
                // client configuration to solve the problem please if SSL is not essential.
                // .enableSsl(false)
//                .setCredentialProvider(sessionCredentialsProvider)
                .build();
        String tag = "tagA";
        FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);
        String consumerGroup = "smsNormalTopic-group";
        String topic = "smsNormalTopic";

            // In most case, you don't need to create too many consumers, singleton pattern is recommended.
            PushConsumer pushConsumer = provider.newPushConsumerBuilder()
                    .setClientConfiguration(clientConfiguration)
//                    .setMaxCacheMessageCount(1)
                    // Set the consumer group name.
                    .setConsumerGroup(consumerGroup)
                    // Set the subscription for the consumer.
                    .setSubscriptionExpressions(Collections.singletonMap(topic, filterExpression))
                    .setMessageListener(messageView -> {
                        // Handle the received message and return consume result.
                        String body = StandardCharsets.UTF_8.decode(messageView.getBody()).toString();
                        System.out.println(DateTimeUtil.datetime18() + " Received message: " + body + ", tag" + messageView.getTag().get());
                        return ConsumeResult.FAILURE;
                    })
                    .build();

        Thread.sleep(200000);

        // Block the main thread, no need for production environment.
//        Thread.sleep(Long.MAX_VALUE);
        // Close the push consumer when you don't need it anymore.
        // You could close it manually or add this into the JVM shutdown hook.
//        pushConsumer.close();
    }
}
