package rabbitMQ.ack;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import rabbitMQ.RabbitMQTestUtil;

public class TxRecv {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = RabbitMQTestUtil.getConnectionFactory();
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        //开启transaction机制
        channel.txSelect();
        channel.queueDeclare("tx_queue", false, false, true, null);

        //关闭自动应答模式(自动应答模式不需要ack、txCommit)，需要手动basicAck，并执行txCommit  
        boolean autoAck = false;
        channel.basicConsume("tx_queue", autoAck, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String mes = new String(body, "UTF-8");
                long deliveryTag = envelope.getDeliveryTag();
                System.out.println("tx Received :'" + mes + "' done" + "deliveryTag=" + deliveryTag);
                if (deliveryTag == 1L) {
                    channel.basicNack(deliveryTag, false, true);
                    channel.txRollback();
                } else {
                    channel.basicAck(deliveryTag, false);
                    channel.txCommit();
                }

            }
        });
    }
}
