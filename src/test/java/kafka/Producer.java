package kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class Producer {

    private static final String TOPIC = "education-info";
    private static final String BROKER_LIST = "10.58.10.103:9192";
    //    private static final String BROKER_LIST="10.57.17.77:9192,10.58.10.103:9192";
    private static KafkaProducer<String, String> producer = null;

    static {
        Properties configs = initConfig();
        producer = new KafkaProducer<String, String>(configs);
    }

    private static Properties initConfig() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", BROKER_LIST);
        properties.put("acks", "0");
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer", StringSerializer.class.getName());
        return properties;
    }

    public static void main(String[] args) {
        try {
            String message = "hello world";
            System.out.println(message);
            ProducerRecord<String, String> record = new ProducerRecord<String, String>(TOPIC, message);
            for (int i = 0; i < 10; i++) {
                producer.send(record);
            }

//            producer.send(record, new Callback() {
//                @Override
//                public void onCompletion(RecordMetadata metadata, Exception exception) {
//                    if(null==exception){
//                        System.out.println("perfect!");
//                    }
//                    if(null!=metadata){
//                        System.out.print("offset:"+metadata.offset()+";partition:"+metadata.partition());
//                    }
//                }
//            }).get();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }
}
