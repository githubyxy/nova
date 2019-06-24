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

public class ConfirmRecv {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = RabbitMQTestUtil.getConnectionFactory();
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        //开启transaction机制
//      channel.confirmSelect();  
        //autoDelete,true 只要服务停止，queue会被删除  
        channel.queueDeclare("tx_queue", false, false, true, null);
        //关闭自动应答模式  
        channel.basicConsume("tx_queue", false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String mes = new String(body, "UTF-8");
                //multiple批量提交，true提交小于参数中tag消息  
                long n = envelope.getDeliveryTag() % 3;
                if (n == 0) {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } else if (n == 1) {
                    //requeue，true重新进入队列  
                    channel.basicNack(envelope.getDeliveryTag(), false, true);
                } else {
                    //requeue，true重新进入队列,与basicNack差异缺少multiple参数  
                    channel.basicReject(envelope.getDeliveryTag(), true);
                }
                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println((n == 0 ? "Ack" : n == 1 ? "Nack" : "Reject") + " mes :'" + mes + "' done");
            }
        });
    }
}
