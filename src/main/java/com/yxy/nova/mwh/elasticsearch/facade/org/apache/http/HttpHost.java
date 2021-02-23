package com.yxy.nova.mwh.elasticsearch.facade.org.apache.http;

import com.yxy.nova.mwh.elasticsearch.facade.container.$;
import com.yxy.nova.mwh.elasticsearch.facade.container.ValueFacade;

/**
 * Created by caipeichao on 17/3/29.
 */
public class HttpHost extends ValueFacade {
    private HttpHost(Object value) {
        super(value);
    }

    public HttpHost(String ip, int port, String http) {
        super($.invoke($.NoPacker, $.MethodHttpHost_new, ip, port, http));
    }
}
