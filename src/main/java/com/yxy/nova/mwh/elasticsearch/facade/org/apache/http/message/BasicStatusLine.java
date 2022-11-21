package com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.message;

import com.yxy.nova.mwh.elasticsearch.facade.container.$;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.ProtocolVersion;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.StatusLine;

/**
 * Created by caipeichao on 2017/5/22.
 */
public class BasicStatusLine extends StatusLine {
    private BasicStatusLine(Object value) {
        super(value);
    }

    public BasicStatusLine(ProtocolVersion version, int statusCode, String reasonPhrase) {
        super($.invoke($.NoPacker, $.MethodBasicStatusLine_new, $.value(version), statusCode, reasonPhrase));
    }
}
