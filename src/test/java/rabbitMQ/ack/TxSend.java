package rabbitMQ.ack;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import rabbitMQ.RabbitMQTestUtil;

public class TxSend {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = RabbitMQTestUtil.getConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //开启transaction机制
        channel.txSelect();
        channel.queueDeclare("tx_queue", false, false, true, null);
        for (int i = 0; i < 10; i++) {
            try {
                channel.basicPublish("", "tx_queue", null, ("Hello World" + i).getBytes());
                //do something
//	                if (i % 2 == 0) {
//		                	int n=5/0;//试验消息回滚  
//	                }
                channel.txCommit();//提交消息
                System.out.println("发布消息" + ("Hello World" + i));
            } catch (Exception e) {
                channel.txRollback();//异常，取消消息
                System.out.println("回滚消息" + ("Hello World" + i));
            }
        }
        Thread.sleep(10000);
        channel.close();
        connection.close();
    }
}
