package com.yxy.nova.mwh.elasticsearch;

import com.yxy.nova.mwh.elasticsearch.admin.Connection;
import com.yxy.nova.mwh.elasticsearch.admin.ConnectionFactory;
import com.yxy.nova.mwh.elasticsearch.basic.select.SearchBuilder;
import com.yxy.nova.mwh.elasticsearch.dto.Config;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.policy.Policy;
import com.yxy.nova.mwh.elasticsearch.policy.PolicyFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 查询信息抽象
 * @author quyuanwen
 */
public abstract class AbstractSearchService implements SearchService{


    private Logger logger = LoggerFactory.getLogger(AbstractSearchService.class);


    protected Config config = new Config();

    protected Policy policy;

//    protected String influxdbURL;

    protected Connection connection;

//    protected MonitorGenerator monitorGenerator;

    private ConnectionFactory connectionFactory ;

    @Override
    public SearchBuilder selectFrom(String table) {
        return new SearchBuilder(this, table);
    }

    @Override
    public SearchBuilder selectFromAll() {
        return this.selectFrom(null);
    }

    /**
     * 初始化
     * @param configJson
     */
    public void setConfigStr(String configJson) {
        JSONObject configObject = JSON.parseObject(configJson);
        if(configObject != null){
            this.config.setClusterName(configObject.getString("clusterName"));
            this.config.setHostList(configObject.getString("hostList"));
            this.config.setUsername(configObject.getString("username"));
            this.config.setPassword(configObject.getString("password"));
            this.config.setTpsLimit(configObject.getIntValue("tpsLimit"));
            this.config.setSearchLimit(configObject.getLongValue("searchLimit"));
            this.config.setPolicy(configObject.getJSONObject("policy"));
        }
    }

    /**
     * 初始化
     */
    @PostConstruct
    public void init() throws ElasticsearchClientException {
        //初始化链接池
        this.connectionFactory = new ConnectionFactory();

        this.connection = connectionFactory.connect(config);

        //策略
        this.policy = PolicyFactory.generate(config,connection);

//        this.monitorGenerator = new MonitorGenerator(influxdbURL, config.getClusterName(), policy);

//        this.monitorGenerator.init();
    }

//    public void setInfluxdbURL(String influxdbURL) {
//        this.influxdbURL = influxdbURL;
//    }

    @PreDestroy
    public void close() {
        try {
            if(connectionFactory != null){
                connectionFactory.close(config.getClusterName());
            }
        } catch (Throwable e) {
            logger.error("Failed to close connection",e);
        }
    }
    @Override
    public Policy getPolicy() {
        return policy;
    }

    public Connection getConnection() {
        return connection;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
