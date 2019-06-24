package kafka;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class Consumer {

    private static final String TOPIC = "education-info";
    // 192.168.6.55:9092,192.168.6.56:9092,192.168.6.57:9092
    private static final String BROKER_LIST = "10.58.10.103:9192";
    //    private static final String BROKER_LIST="10.57.17.77:9192,10.58.10.103:9192";
    private static KafkaConsumer<String, String> kafkaConsumer = null;

    static {
        Properties properties = initConfig();
        kafkaConsumer = new KafkaConsumer<String, String>(properties);
        kafkaConsumer.subscribe(Arrays.asList(TOPIC));
    }

    private static Properties initConfig() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", BROKER_LIST);
        properties.put("group.id", "test");
        properties.put("client.id", "test");
        properties.put("enable.auto.commit", "false");
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer", StringDeserializer.class.getName());
        return properties;
    }

    public static void main(String[] args) {
        try {
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
                for (ConsumerRecord record : records) {
                    try {
                        System.out.println("接受消息：" + record.value());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    kafkaConsumer.commitAsync();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            kafkaConsumer.close();
        }
    }
}
