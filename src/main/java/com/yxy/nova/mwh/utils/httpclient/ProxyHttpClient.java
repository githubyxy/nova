package com.yxy.nova.mwh.utils.httpclient;

import com.yxy.nova.mwh.utils.log.LogUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * HttpClient的代理封装
 * Created by chenchanglong on 2019/7/8.
 */
public class ProxyHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(ProxyHttpClient.class);

    private static final String BOUNDRAY_POST = "proxyHttpPost";
    private static final String BOUNDRAY_POSTJSON = "proxyHttpPostJson";

    private static final Map<ProxyInfo, ProxyHttpClient> clientMap = new HashMap<>(5);

    public static ProxyHttpClient getInstance(ProxyInfo proxyInfo) {
        ProxyHttpClient httpClient = clientMap.get(proxyInfo);
        if(null != httpClient) {
            return httpClient;
        }
        synchronized (proxyInfo) {
            if(null == httpClient) {
                httpClient = new ProxyHttpClient(proxyInfo);
                clientMap.put(proxyInfo, httpClient);
            }
        }
        return httpClient;
    }

    private CloseableHttpClient client = null;

    /**
     * 支持代理的HttpClient
     * 禁止直接使用构造方法去生成实例，使用SimpleHttpClient.getInstance来获取
     * public主要是方便mock
     * @param proxyInfo
     */
    public ProxyHttpClient(ProxyInfo proxyInfo) {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setDefaultMaxPerRoute(30);
        cm.setMaxTotal(150);
        RequestConfig config  = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(30000).setSocketTimeout(30000).build();
        HttpClientBuilder builder = HttpClients.custom().setSSLSocketFactory(SSLConnectionSocketFactory.getSocketFactory()).setDefaultRequestConfig(config).setConnectionManager(cm);
        HttpHost proxy = new HttpHost(proxyInfo.getAddress(), proxyInfo.getPort());
        builder.setProxy(proxy);
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(new AuthScope(proxy), new UsernamePasswordCredentials(proxyInfo.getUser(), proxyInfo.getPassword()));
        builder.setDefaultCredentialsProvider(provider);
        client = builder.build();
    }

    /**
     * 执行post请求
     * @param url
     * @param headerMap
     * @param paramMap
     * @return
     */
    public String post(String url, Map<String, String> headerMap, Map<String, String> paramMap) throws IOException {
        LogUtil.info(logger, "请求的url:{}, headerMap: {}, nameValuePairs: {}", Arrays.asList(url, JSON.toJSONString(headerMap), JSON.toJSONString(paramMap)), BOUNDRAY_POST);

        HttpPost httpPost = new HttpPost(url);

        addHeaders(httpPost, headerMap);
        setEntity(httpPost, paramMap);

        return executeHttpPostRequest(httpPost, url, BOUNDRAY_POST);
    }

    /**
     * 执行post json请求
     * @param url
     * @param headerMap
     * @param paramObj
     * @return
     */
    public String postJson(String url, Map<String, String> headerMap, Object paramObj) throws IOException {

        String jsonStr = JSON.toJSONString(paramObj, SerializerFeature.DisableCircularReferenceDetect);
        LogUtil.info(logger, "请求的url:{}, headerMap: {}, paramObj: {}", Arrays.asList(url, JSON.toJSONString(headerMap), jsonStr), BOUNDRAY_POSTJSON);

        HttpPost httpPost = new HttpPost(url);

        // body为json格式，增加对应header
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        addHeaders(httpPost, headerMap);

        httpPost.setEntity(new StringEntity(jsonStr, Charset.forName("UTF-8")));

        return executeHttpPostRequest(httpPost, url, BOUNDRAY_POSTJSON);
    }

    private String executeHttpPostRequest(HttpPost httpPost, String url, String boundray) throws IOException {
        String result = null;
        CloseableHttpResponse response = null;

        try {
            response = client.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK != statusCode) {
                LogUtil.error(logger, "请求URL {} 时返回的statusCode为 {}", Arrays.asList(url, statusCode), boundray);
            }

            result = EntityUtils.toString(response.getEntity(), "UTF-8");
            LogUtil.info(logger, "服务端返回的response: {}", Arrays.asList(result), boundray);

            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            LogUtil.error(logger, "请求URL {} 时发生错误", Arrays.asList(url, e), boundray);
            throw e;
        } finally {
            if(response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LogUtil.error(logger, "关闭响应流出现错误: {} ", Arrays.asList(e.getMessage(), e), BOUNDRAY_POST);
                }
            }
        }

        return result;
    }

    private void setEntity(HttpPost httpPost, Map<String, String> paramMap) throws UnsupportedEncodingException {
        if(null != paramMap) {
            List<NameValuePair> nameValuePairList = new ArrayList<>(paramMap.size());
            for(Map.Entry<String, String> entry : paramMap.entrySet()) {
                nameValuePairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
        }
    }

    private void addHeaders(HttpPost httpPost, Map<String, String> headerMap) {
        if(null != headerMap) {
            for(Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }
}
