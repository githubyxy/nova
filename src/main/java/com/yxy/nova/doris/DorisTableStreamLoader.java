package com.yxy.nova.doris;

import com.alibaba.fastjson.JSONObject;
import com.yxy.nova.mwh.utils.concurrent.CustomPrefixThreadFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author shui.ren
 * @description: doris table流加载器
 * @date 2023/9/7 3:38 PM
 */
public class DorisTableStreamLoader<T> {
    /**
     * 数据暂存器
     */
    private LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<>(10000);

    @Value("${doris.host}")
    private String host;

    @Value("${doris.http.port}")
    private int port;

    @Value("${doris.http.username}")
    private String username;

    @Value("${doris.http.password}")
    private String password;

    @Value("${doris.db}")
    private String db;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 完整的url
     */
    private String loadUrl;

    private static CloseableHttpClient client;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public DorisTableStreamLoader(String tableName) {
        this.tableName = tableName;
    }

    @PostConstruct
    public void init() {
        this.loadUrl = String.format("http://%s:%s/api/%s/%s/_stream_load", host, port, db, tableName);
        // 创建httpClient
        RequestConfig config  = RequestConfig.custom()
                .setConnectTimeout(5_000)
                .setConnectionRequestTimeout(60_000)
                .setSocketTimeout(60_000)
                .build();
        HttpClientBuilder httpClientBuilder = HttpClients
                .custom()
                .setDefaultRequestConfig(config)
                .setRedirectStrategy(new DefaultRedirectStrategy() {
                    @Override
                    protected boolean isRedirectable(String method) {
                        return true;
                    }
                });
        client = httpClientBuilder.build();
        // 启动定时任务
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1,
                new CustomPrefixThreadFactory("DorisTableStreamLoader-" + tableName));
        scheduler.scheduleAtFixedRate(this::checkAndProcess, 0, 1, TimeUnit.SECONDS);
    }

    public void addData(T data) {
        boolean success = queue.offer(data);
        if (!success) {
            logger.error("{}: Stream load queue is full", tableName);
        }
    }

    private void checkAndProcess() {
        try {
            process();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    private void process() throws IOException {
        if (queue.isEmpty()) {
            return;
        }

        List<T> dataList = new ArrayList<>(queue.size());
        while (!queue.isEmpty() && dataList.size() < 1_000) {
            dataList.add(queue.poll());
        }

        HttpPut put = new HttpPut(loadUrl);
        StringEntity entity = new StringEntity(JSONObject.toJSONString(dataList), "UTF-8");
        put.setHeader(HttpHeaders.EXPECT, "100-continue");
        put.setHeader(HttpHeaders.AUTHORIZATION, basicAuthHeader(username, password));
        put.setHeader("format", "json");
        put.setHeader("strip_outer_array", "true");
        put.setEntity(entity);

        try (CloseableHttpResponse response = client.execute(put)) {
            String loadResult = "";
            if (response.getEntity() != null) {
                loadResult = EntityUtils.toString(response.getEntity());
            }
            final int statusCode = response.getStatusLine().getStatusCode();
            // statusCode 200 just indicates that doris be service is ok, not stream load
            // you should see the output content to find whether stream load is success
            if (statusCode != 200) {
               logger.error("{}: Stream load failed, statusCode={}, load result={}", tableName, statusCode, loadResult);
            } else {
                JSONObject loadResultAsJson = JSONObject.parseObject(loadResult);
                if (StringUtils.equals("Success", loadResultAsJson.getString("Status"))) {
                    logger.info("{}: Stream load success, count={}", tableName, dataList.size());
                } else {
                    logger.error("{}: Stream load failed, load result={}", tableName, loadResult);
                }
            }
        }
    }

    private String basicAuthHeader(String username, String password) {
        final String tobeEncode = username + ":" + StringUtils.trimToEmpty(password);
        byte[] encoded = Base64.encodeBase64(tobeEncode.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encoded);
    }

}
