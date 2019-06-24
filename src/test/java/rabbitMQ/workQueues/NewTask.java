package rabbitMQ.workQueues;

import java.util.Scanner;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import rabbitMQ.RabbitMQTestUtil;

public class NewTask {

    private static final String QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = RabbitMQTestUtil.getConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        boolean durable = true;    //消息持久化
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

        Scanner sc = new Scanner(System.in);

        String[] splits = sc.nextLine().split(" ");
        for (int i = 0; i < splits.length; i++) {
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, splits[i].getBytes());
            System.out.println(" [x] Sent '" + splits[i] + "'");
        }
        Thread.sleep(10000);
        channel.close();
        connection.close();
    }
}
