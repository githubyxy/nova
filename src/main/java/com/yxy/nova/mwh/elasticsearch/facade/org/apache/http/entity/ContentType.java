package com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.entity;

import com.yxy.nova.mwh.elasticsearch.facade.container.$;
import com.yxy.nova.mwh.elasticsearch.facade.container.ValueFacade;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.Consts;

/**
 * Created by caipeichao on 17/3/29.
 */
public class ContentType extends ValueFacade {

    public static final ContentType APPLICATION_JSON = $.invoke($.PackerContentType, $.ClassContentType, $.FieldApplicationJson);

    public static ContentType create(String mime, Consts charset) {
        return $.invoke($.PackerContentType, $.ClassContentType, $.MethodContentType_create, mime, $.value(charset));
    }

    private ContentType(Object value){
        super(value);
    }

    public static ContentType wrap(Object v) {
        return new ContentType(v);
    }
}

