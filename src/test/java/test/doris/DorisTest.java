package test.doris;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import com.yxy.nova.mwh.utils.concurrent.CustomPrefixThreadFactory;
import com.yxy.nova.mwh.utils.time.DateTimeUtil;
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
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DorisTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private JdbcTemplate jdbcTemplate;

    private LinkedBlockingQueue<TestJsonDorisDTO> queue = new LinkedBlockingQueue<>(10000);

    private String host = "114.55.2.52";

    private int port = 8030;

    private String username = "root";

    private String password;

    private String db = "test";

    /**
     * 表名
     */
    private String tableName="test_json";

    /**
     * 完整的url
     */
    private String loadUrl;

    private static CloseableHttpClient client;

    @Before
    public void init() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://114.55.2.52:9030/test?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&connectTimeout=5000&socketTimeout=5000&autoReconnect=true&maxReconnects=5&failOverReadOnly=false&zeroDateTimeBehavior=convertToNull&useSSL=false");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("");
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setInitialSize(3);
        druidDataSource.setMinIdle(1);
        druidDataSource.setMaxActive(50);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setTimeBetweenLogStatsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(300000);
        druidDataSource.setValidationQuery("SELECT 1");
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setPoolPreparedStatements(true);
        druidDataSource.setConnectionInitSqls(Arrays.asList("set session sql_mode=''"));
//        druidDataSource.setFilters("sta");

        jdbcTemplate = new JdbcTemplate(druidDataSource);


        this.loadUrl = String.format("http://%s:%s/api/%s/%s/_stream_load", host, port, db, tableName);
        System.out.println( "loadUrl=" + loadUrl);
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
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1,
//                new CustomPrefixThreadFactory("DorisTableStreamLoader-" + tableName));
//        scheduler.scheduleAtFixedRate(this::checkAndProcess, 0, 1, TimeUnit.SECONDS);
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

        List<TestJsonDorisDTO> dataList = new ArrayList<>(queue.size());
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

    @Test
    public void test() {

        TestJsonDorisDTO testJsonDorisDTO = new TestJsonDorisDTO();
        testJsonDorisDTO.setDs("2024-08-04");
        JSONObject jsonDetail = new JSONObject();
        jsonDetail.put("key1", "value1");
        jsonDetail.put("key2", "value2");
        testJsonDorisDTO.setJsonDetail(jsonDetail.toJSONString());

        testJsonDorisDTO.setGmtCreate(DateTimeUtil.datetime18());
        testJsonDorisDTO.setGmtModify(DateTimeUtil.datetime18());

        boolean success = queue.offer(testJsonDorisDTO);
        if (!success) {
            logger.error("{}: Stream load queue is full", tableName);
        }

        checkAndProcess();

    }

    @Test
    public void select() {
        String sql = "select * from test_json";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        System.out.println(JSONObject.toJSONString(maps));


        String sql2 = "SELECT json_extract(jsonDetail, '$.key1') as key1 FROM test_json";
        List<Map<String, Object>> maps2 = jdbcTemplate.queryForList(sql2);
        System.out.println(JSONObject.toJSONString(maps2));
    }

}
