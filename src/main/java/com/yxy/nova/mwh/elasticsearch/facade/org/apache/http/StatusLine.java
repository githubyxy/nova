package com.yxy.nova.mwh.elasticsearch.facade.org.apache.http;

import com.yxy.nova.mwh.elasticsearch.facade.container.$;
import com.yxy.nova.mwh.elasticsearch.facade.container.ValueFacade;

/**
 * Created by caipeichao on 17/3/29.
 */
public class StatusLine extends ValueFacade {

    protected StatusLine(Object value){
        super(value);
    }

    public int getStatusCode() {
        return $.invoke($.returnInt(), $.value(this), $.MethodStatusLine_getStatusCode);
    }

    public static StatusLine wrap(Object a) {
        return new StatusLine(a);
    }
}
