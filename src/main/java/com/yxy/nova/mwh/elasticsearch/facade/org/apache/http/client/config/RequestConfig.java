package com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.client.config;

import com.yxy.nova.mwh.elasticsearch.facade.container.$;
import com.yxy.nova.mwh.elasticsearch.facade.container.ValueFacade;

/**
 * Created by caipeichao on 17/5/3.
 */
public class RequestConfig extends ValueFacade {
    private RequestConfig(Object value) {
        super(value);
    }

    public static class Builder extends ValueFacade {

        public Builder(Object value) {
            super(value);
        }

        public Builder setConnectTimeout(int x) {
            return $.invoke($.PackerRequestConfigBuilder, $.value(this), $.MethodRequestConfigBuilder_setConnectTimeout, x);
        }

        public Builder setSocketTimeout(int x) {
            return $.invoke($.PackerRequestConfigBuilder, $.value(this), $.MethodRequestConfigBuilder_setSocketTimeout, x);
        }
    }

    public static RequestConfig wrap(Object value) {
        return new RequestConfig(value);
    }
}
