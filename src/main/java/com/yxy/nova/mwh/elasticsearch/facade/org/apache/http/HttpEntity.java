package com.yxy.nova.mwh.elasticsearch.facade.org.apache.http;

import com.yxy.nova.mwh.elasticsearch.facade.container.$;
import com.yxy.nova.mwh.elasticsearch.facade.container.ValueFacade;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by caipeichao on 17/3/29.
 */
public class HttpEntity extends ValueFacade {

    protected HttpEntity(Object value){
        super(value);
    }

    public InputStream getContent() throws IOException {
        return $.invokeThrowsIOException($.PackerInputStream, $.value(this), $.MethodHttpEntity_getContent);
    }

    public static HttpEntity wrap(Object a) {
        return new HttpEntity(a);
    }
}
