package com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.message;

import com.yxy.nova.mwh.elasticsearch.facade.container.$;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.HttpEntity;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.HttpResponse;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.StatusLine;

/**
 * Created by caipeichao on 2017/5/22.
 */
public class BasicHttpResponse extends HttpResponse {
    public BasicHttpResponse(StatusLine statusLine) {
        super($.invoke($.NoPacker, $.MethodBasicHttpResponse_new, $.value(statusLine)));
    }

    private BasicHttpResponse(Object value) {
        super(value);
    }

    public static BasicHttpResponse wrap(Object value) {
        return new BasicHttpResponse(value);
    }

    public void setEntity(HttpEntity entity) {
        $.invoke($.NoPacker, $.value(this), $.MethodBasicHttpResponse_setEntity, $.value(entity));
    }
}
