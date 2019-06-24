package kafka;

import java.util.Properties;

import org.apache.kafka.common.security.JaasUtils;

import kafka.admin.AdminUtils;
import kafka.utils.ZkUtils;

public class TopicCreate {

    private static final String ZK_CONNECT = "192.168.6.55:2181,192.168.6.56:2181,192.168.6.57:2181";
    private static final int SESSION_TIME_OUT = 30000;
    private static final int CONNECT_OUT = 30000;

    public static void createTopic(String topicName, int partitionNumber, int replicaNumber, Properties properties) {
        ZkUtils zkUtils = null;
        try {
            zkUtils = ZkUtils.apply(ZK_CONNECT, SESSION_TIME_OUT, CONNECT_OUT, JaasUtils.isZkSecurityEnabled());
            if (!AdminUtils.topicExists(zkUtils, topicName)) {
                AdminUtils.createTopic(zkUtils, topicName, partitionNumber, replicaNumber, properties, AdminUtils.createTopic$default$6());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            zkUtils.close();
        }
    }

    public static void main(String[] args) {
        createTopic("education-info", 1, 1, new Properties());
    }
}
