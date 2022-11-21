package com.yxy.nova.mwh.elasticsearch.admin;

import com.yxy.nova.mwh.elasticsearch.dto.Config;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.Header;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.HttpHost;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.client.config.RequestConfig;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.message.BasicHeader;
import com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client.Response;
import com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client.RestClient;
import com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client.RestClientBuilder;
import com.yxy.nova.mwh.elasticsearch.util.AssistantUtil;
import com.yxy.nova.mwh.elasticsearch.util.Constants;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ConnectionImpl implements Connection {

    private RestClient client;

    private Logger logger = LoggerFactory.getLogger(ConnectionImpl.class);

    private Config config;

    public ConnectionImpl(Config config) {
        this.config = config;
    }

    public Config getConfig(){
        return config;
    }

    @Override
    public void setConnected(boolean connected) throws ElasticsearchClientException {
        if (isConnected() == connected) return;
        if (connected) {
            connect();
        } else {
            disconnect();
        }
    }

    private void disconnect() {
        // 已经断开连接了
        if (client == null) return;
        // 断开连接
        try {
            client.close();
            this.client = null;
        } catch (IOException e) {
            logger.warn("Failed to close rest client", e);
            this.client = null;
        }
    }

    private void connect() throws ElasticsearchClientException {
        // 连接过了不用再连接
        if (client != null) return;

        // 获取连接配置信息，获取失败就不连接了
        if(config == null) return;

        // 解析主机列表
        List<HttpHost> hosts = parseHostList(config.getHostList());
        HttpHost[] hostsArray = hosts.toArray(new HttpHost[hosts.size()]);

        // 连接搜索引擎
        RestClientBuilder builder = RestClient.builder(hostsArray);
        builder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
            @Override
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                return requestConfigBuilder
                        .setConnectTimeout(5000)
                        .setSocketTimeout(60000);
            }
        });
        builder.setMaxRetryTimeoutMillis(60000);
        Header[] defaultHeaders = getAuthorizationHeaders();
        if (defaultHeaders != null) {
            builder.setDefaultHeaders(defaultHeaders);
        }
        RestClient restClient = builder.build();

        // 连接完成之后检查集群名称是否正确
        for (int i = 0; i < hosts.size() * 3; i++) {
            checkClusterName(restClient, config.getClusterName());
        }

        // 连接成功
        this.client = restClient;
    }

    private Header[] getAuthorizationHeaders() {
        String combine = config.getUsername() + ":" + config.getPassword();
        String auth = "Basic " + base64(combine);
        if (auth == null) return null;
        Header[] result = new Header[1];
        result[0] = new BasicHeader("Authorization", auth);
        return result;
    }

    private String base64(String x) {
        return Base64.encodeBase64String(x.getBytes(Constants.UTF8));
    }

    private void checkClusterName(RestClient client, String expectedClusterName) throws ElasticsearchClientException {
        String actualClusterName = getClusterName(client);
        if (!actualClusterName.equals(expectedClusterName)) {
            String message = String.format("IndicesCache name not match. Try to connect cluster [%s] but it's actually [%s]",
                    expectedClusterName, actualClusterName);
            throw new RuntimeException(message);
        }
    }

    private String getClusterName(RestClient client) throws ElasticsearchClientException {
        Response response = AssistantUtil.performRequest(client, "GET", "/");
        AssistantUtil.checkStatusCode(response);
        JSONObject result = AssistantUtil.parseResponse(response);
        String clusterName = result.getString("cluster_name");
        if (clusterName == null) throw new RuntimeException("Failed to get cluster name");
        return clusterName;
    }

    /**
     * 解析服务器地址
     *
     * @param hostList IP:port，逗号分隔。例如192.168.6.1:9300,192.168.6.12:9300
     */
    private List<HttpHost> parseHostList(String hostList) {
        List<HttpHost> result = new LinkedList<>();
        for (String host : hostList.split(",")) {
            try {
                result.add(parseHost(host));
            } catch (Exception ex) {
                // 输出错误
                logger.error("Failed to parse host: " + host, ex);
            }
        }
        return result;
    }

    private HttpHost parseHost(String host) {
        String[] address = host.split(":");
        String ip = address[0];
        int port = Integer.parseInt(address[1]);
        return new HttpHost(ip, port, "http");
    }

    @Override
    public RestClient getClient() {
        return client;
    }

    @Override
    public boolean isConnected() {
        return this.client != null;
    }

}
