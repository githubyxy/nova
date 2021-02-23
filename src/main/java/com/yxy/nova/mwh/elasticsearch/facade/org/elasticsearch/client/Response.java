package com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client;

import com.yxy.nova.mwh.elasticsearch.facade.container.$;
import com.yxy.nova.mwh.elasticsearch.facade.container.ValueFacade;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.HttpEntity;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.HttpHost;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.HttpResponse;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.StatusLine;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.message.BasicRequestLine;

/**
 * Created by caipeichao on 17/3/29.
 */
public class Response extends ValueFacade {

    public Response(BasicRequestLine requestLine, HttpHost httpHost, HttpResponse httpResponse) {
        super($.invoke($.NoPacker, $.MethodResponse_new, $.value(requestLine), $.value(httpHost), $.value(httpResponse)));
    }

    private Response(Object value){
        super(value);
    }

    public StatusLine getStatusLine() {
        return $.invoke($.PackerStatusLine, $.value(this), $.MethodResponse_getStatusLine);
    }

    public HttpEntity getEntity() {
        return $.invoke($.PackerHttpEntity, $.value(this), $.MethodResponse_getEntity);
    }

    public static Response wrap(Object a) {
        return new Response(a);
    }
}
