package com.yxy.nova.mwh.elasticsearch.facade.org.apache.http;

import com.yxy.nova.mwh.elasticsearch.facade.container.$;
import com.yxy.nova.mwh.elasticsearch.facade.container.ValueFacade;

/**
 * Created by caipeichao on 2017/5/22.
 */
public class ProtocolVersion extends ValueFacade {
    public ProtocolVersion(String protocol, int major, int minor) {
        this(construct(protocol, major, minor));
    }

    private ProtocolVersion(Object value) {
        super(value);
    }

    private static Object construct(String protocol, int major, int minor) {
        return $.invoke($.NoPacker, $.MethodProtocolVersion_new, protocol, major, minor);
    }
}
