package com.yxy.nova.mwh.elasticsearch.basic;

import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client.Response;
import com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client.ResponseException;
import com.yxy.nova.mwh.elasticsearch.util.AssistantUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
import org.elasticsearch.ElasticsearchTimeoutException;
import org.elasticsearch.action.UnavailableShardsException;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.cluster.block.ClusterBlockException;
import org.elasticsearch.index.engine.DocumentMissingException;
import org.elasticsearch.transport.NodeDisconnectedException;
import org.elasticsearch.transport.ReceiveTimeoutTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caipeichao on 15/8/13.
 */
public class ElasticSearchRetry {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchRetry.class);

    public static boolean shouldRetry(Throwable ex) {
        // 分析异常链，如果其中一个异常需要重试，那就重试吧
        List<Throwable> chain = getExceptionChain(ex);
        for (Throwable e : chain) {
            if (shouldRetryNoCause(e)) {
                return true;
            }
        }
        return false;
    }

    public static List<Throwable> getExceptionChain(Throwable ex) {
        List<Throwable> result = new ArrayList<>();
        while (ex != null) {
            result.add(ex);
            ex = ex.getCause();
        }
        return result;
    }

    private static boolean shouldRetryNoCause(Throwable ex) {
        // 连接失败时重试
        if (isException(ex, NoNodeAvailableException.class)) {
            return true;
        }

        // 异步超时重试
        if (isException(ex, ElasticsearchTimeoutException.class)) {
            return true;
        }

        // 集群状态码异常
        if (isException(ex, ClusterBlockException.class)) {
            return true;
        }

        // 无效分片
        if (isException(ex, UnavailableShardsException.class)) {
            return true;
        }

        // 节点无连接
        if (isException(ex, NodeDisconnectedException.class)) {
            return true;
        }

        // 接收超时
        if (isException(ex, ReceiveTimeoutTransportException.class)) {
            return true;
        }

        // 网络异常
        if (isIOException(ex)) {
            return true;
        }

        // 其它错误不重试
        return false;
    }

    private static boolean isIOException(Throwable ex) {
        if (ResponseException.isInstance(ex)) return false;
        if (ex instanceof IOException) return true;
        return false;
    }

    private static boolean isException(Throwable ex, Class<? extends Throwable> x) {
        if (x.isInstance(ex)) {
            return true;
        }
        if (ex instanceof ElasticsearchClientException) {
            String message = ex.getMessage();
            if (message == null) return false;
            if (message.contains(x.getSimpleName())) return true;
        }
        if (ResponseException.isInstance(ex)) {
            ResponseException a = ResponseException.wrap(ex);
            return isExceptionV5(a, x);
        }
        return false;
    }

    public static boolean isDocumentMissing(Throwable ex) {
        List<Throwable> exceptions = getExceptionChain(ex);
        for (Throwable e : exceptions) {
            if (isDocumentMissingNoChain(e)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDocumentMissingNoChain(Throwable ex) {
        if (ex instanceof DocumentMissingException) {
            return true;
        }
        if (ResponseException.isInstance(ex)) {
            return isExceptionV5(ResponseException.wrap(ex), DocumentMissingException.class);
        }
        return false;
    }

    public static boolean shouldRetry(String failureMessage) {
        if (failureMessage == null) return false;
        if (failureMessage.contains("EsRejectedExecutionException")) return true;
        if (failureMessage.contains("es_rejected_execution_exception")) return true;
        // 等待分片恢复之后才能写入
        if (failureMessage.contains("UnavailableShardException")) return true;
        if (failureMessage.contains("unavailable_shard_exception")) return true;
        // 集群通信有问题，写入的时候以为别名不存在，尝试创建索引发现别名是存在的
        if (failureMessage.contains("InvalidIndexNameException")) return true;
        if (failureMessage.contains("invalid_index_name_exception")) return true;
        return false;
    }

    private static boolean isExceptionV5(ResponseException a, Class c) {
        try {
            Response response = a.getResponse();
            JSONObject json = AssistantUtil.parseResponse(response);
            JSONObject error = json.getJSONObject("error");
            JSONArray rootCause = error.getJSONArray("root_cause");
            for (int i = 0; i < rootCause.size(); i++) {
                JSONObject e = rootCause.getJSONObject(i);
                String type = e.getString("type");
                if (type.equals(toCamel(c.getSimpleName()))) return true;
            }
            return false;
        } catch (Throwable ex2) {
            logger.warn("Failed to get response message", ex2);
            return false;
        }
    }

    private static String toCamel(String x) {
        if (x == null) return null;
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, x);
    }
}
