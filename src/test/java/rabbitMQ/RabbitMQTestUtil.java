package rabbitMQ;

import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQTestUtil {

    public static ConnectionFactory getConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.7");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");

        return factory;
    }
}
