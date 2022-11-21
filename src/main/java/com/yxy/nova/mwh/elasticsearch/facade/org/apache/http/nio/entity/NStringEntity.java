package com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.nio.entity;

import com.yxy.nova.mwh.elasticsearch.facade.container.$;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.HttpEntity;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.entity.ContentType;

/**
 * Created by caipeichao on 17/3/29.
 */
public class NStringEntity extends HttpEntity {

    public NStringEntity(String body, ContentType contentType) {
        super($.invoke($.NoPacker, $.MethodNStringEntity_new, body, $.value(contentType)));
    }

    private NStringEntity(Object value) {
        super(value);
    }

    public static NStringEntity wrap(Object v) {
        return new NStringEntity(v);
    }
}
