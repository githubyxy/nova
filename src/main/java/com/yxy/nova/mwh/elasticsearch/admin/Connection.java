package com.yxy.nova.mwh.elasticsearch.admin;

import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client.RestClient;

/**
 * es链接
 * @author quyuanwen
 */
public interface Connection {

    RestClient getClient();

    boolean isConnected();

    void setConnected(boolean connected) throws ElasticsearchClientException;
}
