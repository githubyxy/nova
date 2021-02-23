package com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.message;

import com.yxy.nova.mwh.elasticsearch.facade.container.$;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.Header;

/**
 * Created by caipeichao on 17/3/30.
 */
public class BasicHeader extends Header {
    private BasicHeader(Object value) {
        super(value);
    }

    public BasicHeader(String key, String value) {
        super($.invoke($.NoPacker, $.MethodBasicHeader_new, key, value));
    }
}
