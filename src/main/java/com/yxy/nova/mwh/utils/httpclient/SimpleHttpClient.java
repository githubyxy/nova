package com.yxy.nova.mwh.utils.httpclient;

import com.yxy.nova.mwh.utils.log.LogUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * HttpClient的简单封装
 */
public class SimpleHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(SimpleHttpClient.class);

    /**
     * 连接到某台主机的最大连接数
     */
    private volatile int defaultMaxPerRoute = 30;
    /**
     * 总的最大连接数
     */
    private volatile int maxTotal = 300;
    /**
     * 从连接池中获取连接的最大超时时间，单位：毫秒
     */
    private int connectionRequestTimeout = 60000;
    /**
     * 连接到主机的超时时间，单位：毫秒
     */
    private int connectTimeout = 60000;
    /**
     * 读超时时间
     */
    private int socketTimeout = 60000;

    private CloseableHttpClient client = null;

    public static final SimpleHttpClient getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * 静态持有类
     */
    private static class InstanceHolder {
        private static final SimpleHttpClient instance = new SimpleHttpClient();
    }

    /**
     * 禁止直接使用构造方法去生成实例，使用SimpleHttpClient.getInstance来获取
     * public主要是方便mock
     */
    public SimpleHttpClient() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setDefaultMaxPerRoute(defaultMaxPerRoute);
        cm.setMaxTotal(maxTotal);
        RequestConfig config  = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout)
                .build();
        client = HttpClients.custom()
                .setSSLSocketFactory(SSLConnectionSocketFactory.getSocketFactory())
                .setDefaultRequestConfig(config)
                .setConnectionManager(cm)
                .build();
    }

    /**
     * 执行Get请求
     * @param url
     * @return
     * @throws IOException
     */
    public String get(String url) throws IOException {
        return get(url, new NameValuePair[0]);
    }

    /**
     * 执行get请求
     * @param url
     * @param nameValuePairs
     * @return
     */
    public String get(String url, NameValuePair[] nameValuePairs) throws IOException {
        return get(url, Arrays.asList(nameValuePairs));
    }

    /**
     * 执行get请求
     * @param url
     * @param nameValuePairs
     * @param headerMap
     * @return
     */
    public String get(String url, NameValuePair[] nameValuePairs, Map<String,String> headerMap) throws IOException {
        return get(url, Arrays.asList(nameValuePairs), headerMap);
    }

    /**
     * 执行get请求
     * @param url
     * @param nameValuePairs
     * @return
     */
    public String get(String url, List<NameValuePair> nameValuePairs) throws IOException {
        return get(url, nameValuePairs, null);
    }

    /**
     * 执行get请求
     * @param url
     * @param nameValuePairs
     * @param headerMap
     * @return
     */
    public String get(String url, List<NameValuePair> nameValuePairs, Map<String,String> headerMap) throws IOException {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < nameValuePairs.size(); i++) {
            buf.append(i == 0 ? "?" : "&");
            try {
                buf.append(nameValuePairs.get(i).getName()).append("=")
                        .append(URLEncoder.encode(nameValuePairs.get(i).getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                LogUtil.error(logger, "URL {} 编码时发生错误", Arrays.asList(url, e), "urlEncode");
                return null;
            }
        }
        return get(url + buf.toString(), headerMap);
    }

    /**
     * 执行get请求
     * @param url
     * @param headerMap
     * @return
     */
    public String get(String url, Map<String,String> headerMap) throws IOException {

        LogUtil.info(logger, "请求的url: {}", Arrays.asList(url), "httpGet");

        String result = null;
        CloseableHttpResponse response = null;

        try {
            HttpGet getMethod = new HttpGet(url);
            getMethod.setHeader("Content-Encoding", "UTF-8");

            if (headerMap != null) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    getMethod.setHeader(entry.getKey(), entry.getValue());
                }
            }

            response = client.execute(getMethod);

            int statusCode = response.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK != statusCode) {
                LogUtil.error(logger, "请求URL {} 时返回的statusCode为 {}", Arrays.asList(url, statusCode), "httpGet");
            }

            result = EntityUtils.toString(response.getEntity());
            LogUtil.info(logger, "服务端返回的response: {}", Arrays.asList(result), "httpGet");

            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            LogUtil.error(logger, "请求URL {} 时发生错误", Arrays.asList(url, e), "httpGet");
            throw e;
        } finally {
            if(response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LogUtil.error(logger, "关闭响应流出现错误: {} ", Arrays.asList(e.getMessage(), e), "httpGet");
                }
            }
        }

        return result;
    }

    /**
     * 执行post请求
     * @param url
     * @param nameValuePairs
     * @return
     */
    public String post(String url, NameValuePair[] nameValuePairs) throws IOException {
        return post(url, Arrays.asList(nameValuePairs));
    }

    /**
     * 执行post请求
     * @param url
     * @param nameValuePairs
     * @return
     */
    public String post(String url, List<NameValuePair> nameValuePairs) throws IOException {
        return post(url,nameValuePairs,null);
    }

    /**
     * 执行post请求
     * @param url
     * @param nameValuePairs
     * @param headerMap
     * @return
     * @throws IOException
     */
    public String post(String url, List<NameValuePair> nameValuePairs, Map<String,String> headerMap) throws IOException {
        LogUtil.info(logger, "请求的url:{}, nameValuePairs: {}", Arrays.asList(url, JSON.toJSONString(nameValuePairs)), "httpPost");

        String result = null;
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            if (headerMap != null) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            response = client.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK != statusCode) {
                LogUtil.error(logger, "请求URL {} 时返回的statusCode为 {}", Arrays.asList(url, statusCode), "httpPost");
            }

            result = EntityUtils.toString(response.getEntity(), "UTF-8");
            LogUtil.info(logger, "服务端返回的response: {}", Arrays.asList(result), "httpPost");

            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            LogUtil.error(logger, "请求URL {} 时发生错误", Arrays.asList(url, e), "httpPost");
            throw e;
        } finally {
            if(response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LogUtil.error(logger, "关闭响应流出现错误: {} ", Arrays.asList(e.getMessage(), e), "httpPost");
                }
            }
        }
        return result;
    }

    /**
     * 执行post请求
     * @param url
     * @param jsonObject
     * @return
     */
    public String postJson(String url, Object jsonObject) throws IOException {
        return postJson(url, jsonObject, null);
    }

    /**
     * 执行post请求
     * @param url
     * @param jsonObject
     * @param headerMap
     * @return
     * @throws IOException
     */
    public String postJson(String url, Object jsonObject, Map<String,String> headerMap) throws IOException {
        String jsonString = JSON.toJSONString(jsonObject, SerializerFeature.DisableCircularReferenceDetect);
        return postJsonString(url,jsonString, headerMap);
    }

    /**
     * 执行post请求
     * @param url
     * @param jsonString
     * @return
     */
    public String postJsonString(String url, String jsonString, Map<String,String> headerMap) throws IOException {
        LogUtil.info(logger, "请求的url: {}, 报文体: {}", Arrays.asList(url, jsonString), "httpPostJson");

        String result = null;
        CloseableHttpResponse response = null;
        try {
            HttpEntity entity = new StringEntity(jsonString, Charset.forName("UTF-8"));

            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            if (headerMap != null) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            httpPost.setEntity(entity);
            response = client.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK != statusCode) {
                LogUtil.error(logger, "请求URL {} 时返回的statusCode为 {}", Arrays.asList(url, statusCode), "httpPostJson");
            }

            result = EntityUtils.toString(response.getEntity(), "UTF-8");
            LogUtil.info(logger, "服务端返回的response: {}", Arrays.asList(result), "httpPostJson");

            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            logger.error("请求URL{}时发生错误", url, e);
            throw e;
        } finally {
            if(response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LogUtil.error(logger, "关闭响应流出现错误: {} ", Arrays.asList(e.getMessage(), e), "httpPostJson");
                }
            }
        }
        return result;
    }

    /**
     * 下载文件
     * @param url
     * @param destFile 调用前此文件必须已经存在
     * @throws Exception
     */
    public boolean downloadFile(String url, File destFile) {
        return downloadFile(url, destFile, null);
    }

    /**
     * 通过文件下载URL获取字节数组
     * @param url
     * @param headerMap
     * @return
     */
    public byte[] getByteArray(String url, Map<String,String> headerMap){
        LogUtil.info(logger, "请求的url: {}", Arrays.asList(url), "httpDownload");

        CloseableHttpResponse response = null;
        HttpGet getMethod = new HttpGet(url);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                getMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
        try {
            response = client.execute(getMethod);
            int statusCode = response.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK != statusCode) {
                LogUtil.error(logger, "请求URL {} 时返回的statusCode为 {}", Arrays.asList(url, statusCode), "httpDownload");
                return null;
            }
            response.getEntity().writeTo(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            LogUtil.error(logger, "请求URL {} 时发生错误", Arrays.asList(url, e), "httpDownload");
            return null;
        } finally {
            if(response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LogUtil.error(logger, "关闭响应流出现错误: {} ", Arrays.asList(e.getMessage(), e), "httpPostJson");
                }
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                LogUtil.error(logger, "关闭输出流出现错误: {} ", Arrays.asList(e.getMessage(), e), "httpPostJson");
            }
        }
    }

    /**
     * 通过文件下载URL获取字节数组-post
     *
     * @param url
     * @param jsonObject
     * @return
     */
    public byte[] postByteArray(String url, Object jsonObject, Map<String, String> headerMap) throws IOException {

        LogUtil.info(logger, "请求的url: {}, 报文体: {}", Arrays.asList(url, JSON.toJSONString(jsonObject)), "httpPostByteArray");

        String result = null;
        CloseableHttpResponse response = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            HttpEntity entity = new StringEntity(JSON.toJSONString(jsonObject,
                    SerializerFeature.DisableCircularReferenceDetect),
                    Charset.forName("UTF-8"));
            HttpPost httpPost = new HttpPost(url);
            if (headerMap != null) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            httpPost.setEntity(entity);
            response = client.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK != statusCode) {
                LogUtil.error(logger, "请求URL {} 时返回的statusCode为 {}", Arrays.asList(url, statusCode),
                        "httpPost");
                EntityUtils.consume(response.getEntity());
                return null;
            }

            response.getEntity().writeTo(outputStream);
            EntityUtils.consume(response.getEntity());
            return outputStream.toByteArray();
        } catch (IOException e) {
            LogUtil.error(logger, "请求URL {} 时发生错误", Arrays.asList(url, e), "httpPostByteArray");
            throw e;
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LogUtil.error(logger, "关闭响应流出现错误: {} ", Arrays.asList(e.getMessage(), e),
                            "httpPost");
                }
            }
        }
    }

    /**
     * 下载文件
     * @param url
     * @param destFile 调用前此文件必须已经存在
     * @param headerMap 调用的请求头
     * @throws Exception
     */
    public boolean downloadFile(String url, File destFile, Map<String,String> headerMap) {
        LogUtil.info(logger, "请求的url: {}, 文件名: {}", Arrays.asList(url, destFile.getName()), "httpDownload");

        CloseableHttpResponse response = null;
        try {
            HttpGet getMethod = new HttpGet(url);
            if (headerMap != null) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    getMethod.setHeader(entry.getKey(), entry.getValue());
                }
            }
            response = client.execute(getMethod);

            int statusCode = response.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK != statusCode) {
                LogUtil.error(logger, "请求URL {} 时返回的statusCode为 {}", Arrays.asList(url, statusCode), "httpDownload");
                return false;
            }

            FileOutputStream fileOutputStream = new FileOutputStream(destFile);
            response.getEntity().writeTo(fileOutputStream);

            return true;
        } catch (IOException e) {
            LogUtil.error(logger, "请求URL {} 时发生错误", Arrays.asList(url, e), "httpDownload");
            return false;
        } finally {
            if(response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LogUtil.error(logger, "关闭响应流出现错误: {} ", Arrays.asList(e.getMessage(), e), "httpDownload");
                }
            }
        }

    }

    public int getDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }

    public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public CloseableHttpClient getClient() {
        return client;
    }

    public void setClient(CloseableHttpClient client) {
        this.client = client;
    }
}
