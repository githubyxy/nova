package com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client;

import com.yxy.nova.mwh.elasticsearch.facade.container.$;
import com.yxy.nova.mwh.elasticsearch.facade.container.ValueFacade;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.Header;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.client.config.RequestConfig;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by caipeichao on 17/3/29.
 */
public class RestClientBuilder extends ValueFacade {

    private RestClientBuilder(Object value){
        super(value);
    }

    public RestClientBuilder setMaxRetryTimeoutMillis(int t) {
        return $.invoke($.PackerRestClientBuilder, $.value(this), $.MethodRestClientBuilder_setMaxRetryTimeoutMillis, t);
    }

    public RestClientBuilder setDefaultHeaders(Header[] defaultHeaders) {
        return $.invoke($.PackerRestClientBuilder, $.value(this), $.MethodRestClientBuilder_setDefaultHeaders,
                        $.valueArray(defaultHeaders));
    }

    public RestClient build() {
        return $.invoke($.PackerRestClient, $.value(this), $.MethodRestClientBuilder_build);
    }

    public RestClientBuilder setRequestConfigCallback(RequestConfigCallback callback) {
        return $.invoke($.PackerRestClientBuilder, $.value(this), $.MethodRestClientBuilder_setRequestConfigCallback,
                        proxy(callback));
    }

    public static RestClientBuilder wrap(Object v) {
        return new RestClientBuilder(v);
    }

    public static Object proxy(RequestConfigCallback callback) {
        Object result = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { $.ClassRequestConfigCallback },
                                               new ProxyRequestConfigCallback(callback));
        return result;
    }

    public static class ProxyRequestConfigCallback implements InvocationHandler {

        private final RequestConfigCallback base;

        public ProxyRequestConfigCallback(RequestConfigCallback base){
            this.base = base;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.equals($.MethodRequestConfigCallback_customizeRequestConfig)) {
                return $.value(base.customizeRequestConfig(new RequestConfig.Builder(args[0])));
            }
            throw new RuntimeException("Unknown invocation: " + method);
        }
    }

    public interface RequestConfigCallback {

        public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder builder);
    }
}
