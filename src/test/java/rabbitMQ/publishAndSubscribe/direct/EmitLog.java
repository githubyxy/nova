package rabbitMQ.publishAndSubscribe.direct;

import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import rabbitMQ.RabbitMQTestUtil;

public class EmitLog {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv)
            throws java.io.IOException, TimeoutException {

        ConnectionFactory factory = RabbitMQTestUtil.getConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        // 多个消息使用空格分隔
        Scanner sc = new Scanner(System.in);
        String[] splits = sc.nextLine().split(" ");
        for (int i = 0; i < splits.length; i++) {
            channel.basicPublish(EXCHANGE_NAME, splits[i], null, splits[i].getBytes());
            System.out.println(" [x] Sent '" + splits[i] + "'");
        }

        channel.close();
        connection.close();
    }
}