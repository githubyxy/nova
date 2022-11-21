package com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client;

import com.yxy.nova.mwh.elasticsearch.facade.container.$;
import com.yxy.nova.mwh.elasticsearch.facade.container.ValueFacade;

/**
 * Created by caipeichao on 17/5/11.
 */
public class ResponseException extends ValueFacade {
    private ResponseException(Object x) {
        super(x);
    }

    public ResponseException(Response response) {
        super($.invoke($.NoPacker, $.MethodResponseException_new, $.value(response)));
    }

    public static boolean isInstance(Object o) {
        return $.ClassResponseException.isInstance(o);
    }

    public Response getResponse() {
        return $.invoke($.PackerResponse, $.value(this), $.MethodResponseException_getResponse);
    }

    public static ResponseException wrap(Object v) {
        return new ResponseException(v);
    }
}
