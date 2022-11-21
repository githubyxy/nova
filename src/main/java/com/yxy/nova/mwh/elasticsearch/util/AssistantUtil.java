package com.yxy.nova.mwh.elasticsearch.util;

import com.yxy.nova.mwh.elasticsearch.admin.Connection;
import com.yxy.nova.mwh.elasticsearch.exception.ESExceptionType;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.HttpEntity;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.StatusLine;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.entity.ContentType;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.nio.entity.NStringEntity;
import com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client.Response;
import com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client.RestClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLEncoder;
import java.util.Collections;

public class AssistantUtil {
    private static Logger logger = LoggerFactory.getLogger(AssistantUtil.class);

    /**
     * 校验返回结果
     * @param response
     * @throws ElasticsearchClientException
     */
    public static void checkStatusCode(Response response) throws ElasticsearchClientException {
        if (!isStatusOk(response)) {
            throw new ElasticsearchClientException("请求失败", ESExceptionType.REQUEST_ERROR);
        }
    }

    public static int getStatusCode(Response response) throws ElasticsearchClientException{
        if (response == null) return 0;
        StatusLine statusLine = response.getStatusLine();
        if (statusLine == null) throw new ElasticsearchClientException("获取响应状态码失败", ESExceptionType.RESPONSE_ERROR);
        return statusLine.getStatusCode();
    }

    public static Response performRequest(Connection connection, String method, String path) throws ElasticsearchClientException{
        ESLogger.curlLog(connection, method ,path, "");
        return performRequest(connection.getClient(),method,path);
    }

    public static Response performRequest(RestClient client, String method, String path) throws ElasticsearchClientException{
        try {
            return client.performRequest(method, path);
        } catch (IOException e) {
            throw new ElasticsearchClientException("执行请求失败", ESExceptionType.REQUEST_ERROR,e);
        }
    }

    public static Response performRequest(Connection connection, String method, String path, String body) throws ElasticsearchClientException{
        ESLogger.curlLog(connection, method ,path, body);
        return performRequest(connection.getClient(),method,path,body);
    }

    public static Response performRequest(RestClient client, String method, String path, String body) throws ElasticsearchClientException{
        try {
            NStringEntity entity = new NStringEntity(body, Constants.TEXT_PLAIN_UTF8);
            return client.performRequest(method, path, Collections.<String, String> emptyMap(), entity);
        } catch (IOException e) {
            throw new ElasticsearchClientException("执行请求失败", ESExceptionType.REQUEST_ERROR,e);
        }
    }

    public static Response performRequest(Connection connection, String method, String path, JSONObject body) throws ElasticsearchClientException{
        ESLogger.curlLog(connection, method ,path, body);
        return performRequest(connection.getClient(),method,path,body);
    }

    public static Response performRequest(RestClient client, String method, String path, JSONObject body) throws ElasticsearchClientException{
        try {
            String bodyString = toJSONString(body);
            NStringEntity entity = new NStringEntity(bodyString, ContentType.APPLICATION_JSON);
            return client.performRequest(method, path, Collections.<String, String> emptyMap(), entity);
        } catch (IOException e) {
            throw new ElasticsearchClientException("执行请求失败", ESExceptionType.REQUEST_ERROR,e);
        }
    }

    public static JSONObject parseResponse(Response response) throws ElasticsearchClientException{

        return parseResponse(response, false);
    }

    public static JSONObject parseResponse(Response response, boolean printLog) throws ElasticsearchClientException{
        HttpEntity entity = response.getEntity();
        if (entity == null)
            throw new ElasticsearchClientException("服务没有响应", ESExceptionType.RESPONSE_ERROR);
        String bodyString = getBodyString(entity);
        if (bodyString == null)
            throw new ElasticsearchClientException("读取响应信息为空", ESExceptionType.RESPONSE_ERROR);
        if(printLog){
            logger.info("返回值解析结果为："+bodyString);
        }
        Object result = JSONObject.parse(bodyString, Constants.JSON_FEATURE);
        if (!(result instanceof JSONObject))
            throw new ElasticsearchClientException("响应结果非JSON对象，实际对象:"+result.getClass().getCanonicalName(), ESExceptionType.RESPONSE_ERROR);

        return (JSONObject) result;
    }

    public static JSONArray parseResponse2Array(Response response) throws ElasticsearchClientException{
        return parseResponse2Array(response, false);
    }

    public static JSONArray parseResponse2Array(Response response, boolean printLog) throws ElasticsearchClientException{

        HttpEntity entity = response.getEntity();
        if (entity == null)
            throw new ElasticsearchClientException("服务没有响应", ESExceptionType.RESPONSE_ERROR);
        String bodyString = getBodyString(entity);
        if (bodyString == null)
            throw new ElasticsearchClientException("读取响应信息为空", ESExceptionType.RESPONSE_ERROR);
        if(printLog){
            logger.info("返回值解析结果为："+bodyString);
        }
        Object result = JSONArray.parse(bodyString, Constants.JSON_FEATURE);
        if (!(result instanceof JSONArray))
            throw new ElasticsearchClientException("响应结果非JSONArray对象，实际对象:"+result.getClass().getCanonicalName(), ESExceptionType.RESPONSE_ERROR);

        return (JSONArray) result;
    }


    private static String getBodyString(HttpEntity entity) throws ElasticsearchClientException{
        byte[] bytes = getBodyBytes(entity);
        return new String(bytes, Constants.UTF8);
    }

    private static byte[] getBodyBytes(HttpEntity entity) throws ElasticsearchClientException{
        InputStream input = null;
        try {
            input = entity.getContent();
            return readInputStream(input);
        } catch (IOException e) {
            throw new ElasticsearchClientException("获取请求响应内容失败", ESExceptionType.RESPONSE_ERROR, e);
        } finally {
            closeStream(input);
        }
    }

    private static byte[] readInputStream(InputStream inputStream) throws ElasticsearchClientException{
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            copy(inputStream, outputStream, 2048);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new ElasticsearchClientException("读取输入流失败", ESExceptionType.STREAM_ERROR, e);
        }
    }

    private static void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        byte[] buf = new byte[bufferSize];
        for (int bytesRead = input.read(buf); bytesRead != -1; bytesRead = input.read(buf)) {
            output.write(buf, 0, bytesRead);
        }
        output.flush();
    }

    private static void closeStream(InputStream stream) throws ElasticsearchClientException{
        if (stream == null) return;
        try {
            stream.close();
        } catch (IOException e) {
            throw new ElasticsearchClientException("关闭输入流失败", ESExceptionType.STREAM_ERROR, e);
        }
    }

    public static String urlEncode(String url) {
        try {
            return URLEncoder.encode(url, "utf8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJSONString(JSONObject json) {
        return JSON.toJSONString(json, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static boolean isStatusOk(Response response) throws ElasticsearchClientException{
        int statusCode = getStatusCode(response);
        if (statusCode < 200) {
            return false;
        }
        if (statusCode >= 300) {
            return false;
        }
        return true;
    }
}
