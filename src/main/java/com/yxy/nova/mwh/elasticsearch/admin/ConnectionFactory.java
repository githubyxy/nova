package com.yxy.nova.mwh.elasticsearch.admin;

import com.yxy.nova.mwh.elasticsearch.dto.Config;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;

import java.util.HashMap;
import java.util.Map;

/**
 * 链接管理
 * @author quyuanwen
 */
public class ConnectionFactory {


    private final Map<String, Connection> clusterMap = new HashMap<>();

    /**
     * 创建链接
     * @param config
     * @throws ElasticsearchClientException
     */
    public Connection connect(Config config) throws ElasticsearchClientException {
        Connection connection = clusterMap.get(config.getClusterName());
        if(connection == null){
            synchronized (ConnectionFactory.class){
                connection = clusterMap.get(config.getClusterName());
                if(connection == null) {
                    connection = new ConnectionImpl(config);
                    connection.setConnected(true);
                    clusterMap.put(config.getClusterName(), connection);
                }
            }
        }
        return connection;
    }

    /**
     * 关闭集群链接
     * @param clusterName
     * @return
     * @throws ElasticsearchClientException
     */
    public void close(String clusterName) throws ElasticsearchClientException {
        Connection connection = clusterMap.get(clusterName);
        if(connection != null){
            connection.setConnected(false);
            clusterMap.remove(clusterName);
        }
    }
}
