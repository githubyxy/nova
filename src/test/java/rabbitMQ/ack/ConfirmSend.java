package rabbitMQ.ack;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import rabbitMQ.RabbitMQTestUtil;

public class ConfirmSend {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = RabbitMQTestUtil.getConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //开启transaction机制
        channel.confirmSelect();
        channel.queueDeclare("tx_queue", false, false, true, null);
        //异步实现发送消息的确认(此部分的消息确认是指发送消息到队列，并非确认消息的有效消费)  
        channel.addConfirmListener(new ConfirmListener() {

            @Override
            public void handleNack(long deliveryTag, boolean multiple)
                    throws IOException {
                //multiple：测试发现multiple随机true或false，原因未知  
                System.out.println("Nack deliveryTag:" + deliveryTag + ",multiple:" + multiple);
            }

            @Override
            public void handleAck(long deliveryTag, boolean multiple)
                    throws IOException {
                System.out.println("Ack deliveryTag:" + deliveryTag + ",multiple:" + multiple);
            }
        });
        for (int i = 0; i < 10; i++) {
            channel.basicPublish("", "tx_queue", null, ("Hello World" + i).getBytes());
        }
//      channel.waitForConfirms();//同步实现发送消息的确认  
        System.out.println("-----------");
        channel.close();
        connection.close();
    }
}
