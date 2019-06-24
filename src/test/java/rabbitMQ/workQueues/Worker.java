package rabbitMQ.workQueues;

import com.rabbitmq.client.*;

import rabbitMQ.RabbitMQTestUtil;

import java.io.IOException;

public class Worker {

    private final static String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = RabbitMQTestUtil.getConnectionFactory();
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // basicQos方法来设置prefetchCount = 1。 这告诉RabbitMQy一次只给worker一条消息，换句话来说，就是直到worker发回ack，然后再向这个worker发送下一条消息。
        channel.basicQos(1);

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

                System.out.println(" [x] Received '" + message + "'");
                try {
                    doWork(message);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    channel.basicRecover(true);
                    e.printStackTrace();
                } finally {
                    long deliveryTag = envelope.getDeliveryTag();
                    System.out.println(" [x] Done" + deliveryTag);
                    channel.basicAck(deliveryTag, false);
                }
            }
        };
        // 当consumer确认收到某个消息，并且已经处理完成，RabbitMQ可以删除它时，consumer会向RabbitMQ发送一个ack(nowledgement)。
        boolean autoAck = true;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, consumer);
    }

    protected static void doWork(String message) throws InterruptedException {
        for (char ch : message.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }
}
