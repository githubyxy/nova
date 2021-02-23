package com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client;

import com.yxy.nova.mwh.elasticsearch.facade.container.$;
import com.yxy.nova.mwh.elasticsearch.facade.container.ValueFacade;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.HttpHost;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.nio.entity.NStringEntity;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Map;

/**
 * Created by caipeichao on 17/3/29.
 */
public class RestClient extends ValueFacade {

    private RestClient(Object value){
        super(value);
    }

    public Response performRequest(String method, String path) throws IOException {
        Object headers = Array.newInstance($.ClassHeader, 0);
        return $.invokeThrowsIOException($.PackerResponse, $.value(this), $.MethodRestClient_performRequest3, method, path, headers);
    }

    public Response performRequest(String method, String path, Map<String, String> stringStringMap, NStringEntity entity)
                                                                                                                         throws IOException {
        Object headers = Array.newInstance($.ClassHeader, 0);
        return $.invokeThrowsIOException($.PackerResponse, $.value(this), $.MethodRestClient_performRequest5, method, path, stringStringMap, $.value(entity), headers);
    }

    public void close() throws IOException {
        $.invokeThrowsIOException($.PackerVoid, $.value(this), $.MethodRestClient_close);
    }

    public static RestClientBuilder builder(HttpHost[] hostsArray) {
        return $.invoke($.PackerRestClientBuilder, $.ClassRestClient, $.MethodRestClient_builder, $.valueArray(hostsArray));
    }

    public static RestClient wrap(Object a) {
        return new RestClient(a);
    }
}
