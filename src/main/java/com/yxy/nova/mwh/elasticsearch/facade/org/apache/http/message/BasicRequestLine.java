package com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.message;

import com.yxy.nova.mwh.elasticsearch.facade.container.$;
import com.yxy.nova.mwh.elasticsearch.facade.container.ValueFacade;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.ProtocolVersion;

/**
 * Created by caipeichao on 2017/5/22.
 */
public class BasicRequestLine extends ValueFacade {
    private BasicRequestLine(Object value) {
        super(value);
    }

    public BasicRequestLine(String method, String uri, ProtocolVersion protocolVersion) {
        super($.invoke($.NoPacker, $.MethodBasicRequestLine_new, method, uri, $.value(protocolVersion)));
    }
}
