package com.yxy.nova.mwh.elasticsearch.facade.org.apache.http;

import com.yxy.nova.mwh.elasticsearch.facade.container.$;
import com.yxy.nova.mwh.elasticsearch.facade.container.ValueFacade;

/**
 * Created by caipeichao on 17/3/29.
 */
public class Consts extends ValueFacade {

    public static final Consts UTF_8 = $.invoke($.PackerConsts, $.ClassConsts, $.FieldUtf8);

    private Consts(Object value){
        super(value);
    }

    public static Consts wrap(Object a) {
        return new Consts(a);
    }
}
