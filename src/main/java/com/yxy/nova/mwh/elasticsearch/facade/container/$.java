package com.yxy.nova.mwh.elasticsearch.facade.container;

import com.yxy.nova.mwh.elasticsearch.basic.ElasticSearchRetry;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.*;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.client.config.RequestConfig;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.entity.ContentType;
import com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client.Response;
import com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client.RestClient;
import com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.index.engine.DocumentMissingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by caipeichao on 17/3/29.
 */
public class $ {

    private static final Logger logger = LoggerFactory.getLogger($.class);
    public static final Packer<Void> PackerVoid = new Packer<Void>() {

        @Override
        public Void pack(Object a) {
            return null;
        }
    };
    public static final Class ClassConsts = loadClass("org.apache.http.Consts");
    public static final Field FieldUtf8 = getField(ClassConsts, "UTF_8");
    public static final Class ClassContentType = loadClass("org.apache.http.entity.ContentType");
    public static final Method MethodContentType_create = getMethod(ClassContentType, "create", String.class, Charset.class);
    public static final Class ClassResponse = loadClass("org.elasticsearch.client.Response");
    public static final Method MethodResponse_getStatusLine = getMethod(ClassResponse, "getStatusLine");
    public static final Class ClassStatusLine = loadClass("org.apache.http.StatusLine");
    public static final Method MethodStatusLine_getStatusCode = getMethod(ClassStatusLine, "getStatusCode");
    public static final Class ClassRestClient = loadClass("org.elasticsearch.client.RestClient");
    public static final Class ClassHeaderArray = loadArray("org.apache.http.Header");
    public static final Method MethodRestClient_performRequest3 = getMethod(ClassRestClient, "performRequest", String.class, String.class, ClassHeaderArray);
    public static final Class ClassNStringEntity = loadClass("org.apache.http.nio.entity.NStringEntity");
    public static final Constructor MethodNStringEntity_new = getConstructor(ClassNStringEntity, String.class, ClassContentType);
    public static final Class ClassHttpEntity = loadClass("org.apache.http.HttpEntity");
    public static final Method MethodRestClient_performRequest5 = getMethod(ClassRestClient, "performRequest", String.class, String.class, Map.class, ClassHttpEntity, ClassHeaderArray);
    public static final Packer<ContentType> PackerContentType = new Packer<ContentType>() {
        @Override
        public ContentType pack(Object a) {
            return ContentType.wrap(a);
        }
    };

    public static final Packer<Consts> PackerConsts = new Packer<Consts>() {
        @Override
        public Consts pack(Object a) {
            return Consts.wrap(a);
        }
    };
    public static final Packer<StatusLine> PackerStatusLine = new Packer<StatusLine>() {
        @Override
        public StatusLine pack(Object a) {
            return StatusLine.wrap(a);
        }
    };
    public static final Field FieldApplicationJson = getField(ClassContentType, "APPLICATION_JSON");
    public static final Packer<HttpEntity> PackerHttpEntity = new Packer<HttpEntity>() {
        @Override
        public HttpEntity pack(Object a) {
            return HttpEntity.wrap(a);
        }
    };
    public static final Method MethodResponse_getEntity = getMethod(ClassResponse, "getEntity");
    public static final Method MethodHttpEntity_getContent = getMethod(ClassHttpEntity, "getContent");
    public static final Packer<InputStream> PackerInputStream = new Packer<InputStream>() {
        @Override
        public InputStream pack(Object x) {
            return (InputStream) x;
        }
    };
    public static final Packer<Response> PackerResponse = new Packer<Response>() {
        @Override
        public Response pack(Object a) {
            return Response.wrap(a);
        }
    };
    public static final Method MethodRestClient_close = getMethod(ClassRestClient, "close");
    public static final Class ClassHttpHostArray = loadArray("org.apache.http.HttpHost");
    public static final Method MethodRestClient_builder = getMethod(ClassRestClient, "builder", ClassHttpHostArray);
    public static final Packer<RestClientBuilder> PackerRestClientBuilder = new Packer<RestClientBuilder>() {
        @Override
        public RestClientBuilder pack(Object a) {
            return RestClientBuilder.wrap(a);
        }
    };
    public static final Class ClassHttpHost = loadClass("org.apache.http.HttpHost");
    public static final Class ClassRestClientBuilder = loadClass("org.elasticsearch.client.RestClientBuilder");
    public static final Method MethodRestClientBuilder_setMaxRetryTimeoutMillis = getMethod(ClassRestClientBuilder, "setMaxRetryTimeoutMillis", int.class);
    public static final Method MethodRestClientBuilder_setDefaultHeaders = getMethod(ClassRestClientBuilder, "setDefaultHeaders", ClassHeaderArray);
    public static final Class ClassHttpClientConfigCallback = subClass(ClassRestClientBuilder, "HttpClientConfigCallback");
     public static final Packer<RestClient> PackerRestClient = new Packer<RestClient>() {
        @Override
        public RestClient pack(Object a) {
            return RestClient.wrap(a);
        }
    };
    public static final Method MethodRestClientBuilder_build = getMethod(ClassRestClientBuilder, "build");
    public static final Packer<Object> NoPacker = new Packer<Object>() {
        @Override
        public Object pack(Object a) {
            return a;
        }
    };
    public static final Class ClassBasicHeader = loadClass("org.apache.http.message.BasicHeader");
    public static final Constructor MethodBasicHeader_new = getConstructor(ClassBasicHeader, String.class, String.class);
    public static final Constructor MethodHttpHost_new = getConstructor(ClassHttpHost, String.class, int.class, String.class);
    public static final Class ClassHeader = loadClass("org.apache.http.Header");
    public static final Class ClassRequestConfig = loadClass("org.apache.http.client.config.RequestConfig");
    public static final Class ClassRequestConfigBuilder = subClass(ClassRequestConfig, "Builder");
    static {
        System.out.println(ClassHttpClientConfigCallback);
    }
    public static final Packer<RequestConfig.Builder> PackerRequestConfigBuilder = new Packer<RequestConfig.Builder>() {

        @Override
        public RequestConfig.Builder pack(Object a) {
            return new RequestConfig.Builder(a);
        }
    };
    public static final Method MethodRequestConfigBuilder_setConnectTimeout = getMethod(ClassRequestConfigBuilder, "setConnectTimeout", int.class);
    public static final Method MethodRequestConfigBuilder_setSocketTimeout = getMethod(ClassRequestConfigBuilder, "setSocketTimeout", int.class);
    public static final Class ClassRequestConfigCallback = subClass(ClassRestClientBuilder, "RequestConfigCallback");
    public static final Method MethodRestClientBuilder_setRequestConfigCallback = getMethod(ClassRestClientBuilder, "setRequestConfigCallback", ClassRequestConfigCallback);
    public static final Method MethodRequestConfigCallback_customizeRequestConfig = getMethod(ClassRequestConfigCallback, "customizeRequestConfig", ClassRequestConfigBuilder);
    public static final Class ClassResponseException = loadClass("org.elasticsearch.client.ResponseException");
    public static final Method MethodResponseException_getResponse = getMethod(ClassResponseException, "getResponse");
    public static final Class ClassProtocolVersion = loadClass("org.apache.http.ProtocolVersion");
    public static final Constructor MethodProtocolVersion_new = getConstructor(ClassProtocolVersion, String.class, int.class, int.class);
    public static final Class ClassBasicStatusLine = loadClass("org.apache.http.message.BasicStatusLine");
    public static final Constructor MethodBasicStatusLine_new = getConstructor(ClassBasicStatusLine, ClassProtocolVersion, int.class, String.class);
    public static final Class ClassBasicHttpResponse = loadClass("org.apache.http.message.BasicHttpResponse");
    public static final Constructor MethodBasicHttpResponse_new = getConstructor(ClassBasicHttpResponse, ClassStatusLine);
    public static final Class ClassBasicRequestLine = loadClass("org.apache.http.message.BasicRequestLine");
    public static final Constructor MethodBasicRequestLine_new = getConstructor(ClassBasicRequestLine, String.class, String.class, ClassProtocolVersion);
    public static final Class ClassRequestLine = loadClass("org.apache.http.RequestLine");
    public static final Class ClassHttpResponse = loadClass("org.apache.http.HttpResponse");
    public static final Constructor MethodResponse_new = getConstructor(ClassResponse, ClassRequestLine, ClassHttpHost, ClassHttpResponse);
    public static final Method MethodBasicHttpResponse_setEntity = getMethod(ClassBasicHttpResponse, "setEntity", ClassHttpEntity);
    public static final Constructor MethodResponseException_new = getConstructor(ClassResponseException, ClassResponse);

    private static Constructor getConstructor(Class c, Class... args) {
        try {
            Constructor result = c.getDeclaredConstructor(args);
            if(!result.isAccessible()) result.setAccessible(true);
            return result;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static Class loadArray(String className) {
        return loadClass("[L" + className + ";");
    }

    private static Class loadClass(String className) {
        try {
            //return Class.forName(className, true, ESClassLoader.INSTANCE);
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static Field getField(Class c, String field) {
        try {
            return c.getField(field);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static Method getMethod(Class c, String method, Class... args) {
        try {
            return c.getMethod(method, args);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T invoke(Packer<T> packer, Object instance, Method m, Object... args) {
        // 调用
        Object result = null;
        try {
            result = m.invoke(instance, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            handleException(e);
        }

        // 返回结果
        if (result == null) return null;
        return packer.pack(result);
    }

    private static void handleException(InvocationTargetException e) {
        Throwable t = e.getTargetException();
        if (t == null) {
            throw new RuntimeException(e);
        }
        if (t instanceof RuntimeException) {
            logger.warn("Rethrowing", t);
            throw (RuntimeException) t;
        }
        logger.warn("Convert to RuntimeException", t);
        throw new RuntimeException(t);
    }

    private static void handleIOException(InvocationTargetException e) throws IOException {
        Throwable t = e.getTargetException();
        if (t == null) {
            throw new RuntimeException(e);
        }
        if (t instanceof RuntimeException) {
            logger.warn("Rethrowing", t);
            throw (RuntimeException) t;
        }
        if (t instanceof IOException) {
            if(ElasticSearchRetry.isDocumentMissing(t)) {
                throw new DocumentMissingException(null, null, null);
            }
            logger.warn("Rethrowing", t);
            throw (IOException) t;
        }
        logger.warn("Convert to RuntimeException", t);
        throw new RuntimeException(t);
    }

    public static <T> T invoke(Packer<T> packer, Constructor c, Object... args) {
        // 调用
        Object result = null;
        try {
            result = c.newInstance(args);
        } catch (InvocationTargetException e) {
            handleException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // 返回结果
        return packer.pack(result);
    }

    public static Packer<Integer> returnInt() {
        return new Packer<Integer>() {

            @Override
            public Integer pack(Object a) {
                if (!(a instanceof Integer)) {
                    throw new RuntimeException("Expect Integer but got: " + a.getClass().getCanonicalName());
                }
                return (Integer) a;
            }
        };
    }

    public static <T> T invokeThrowsIOException(Packer<T> packer, Object instance, Method m, Object... args)
            throws IOException {
        // 调用
        Object result = null;
        try {
            result = m.invoke(instance, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            handleIOException(e);
        }

        // 返回结果
        if (result == null) return null;
        return packer.pack(result);
    }

    public static <T> T invoke(Packer<T> packer, Object instance, Field f) {
        Object result = null;
        try {
            result = f.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if (result == null) return null;
        return packer.pack(result);
    }

    public static Object value(ValueFacade facade) {
        if (facade == null) return null;
        return facade.value;
    }

    public static Object valueArray(HttpHost[] array) {
        if (array == null) return null;
        Object result = Array.newInstance(ClassHttpHost, array.length);
        for (int i = 0; i < array.length; i++) {
            Array.set(result, i, value(array[i]));
        }
        return result;
    }

    public static Object valueArray(Header[] array) {
        if (array == null) return null;
        Object result = Array.newInstance(ClassHeader, array.length);
        for (int i = 0; i < array.length; i++) {
            Object v = value(array[i]);
            Array.set(result, i, v);
        }
        return result;
    }

    public static Class subClass(Class parent, String sub) {
        for(Class c : parent.getClasses()) {
            if(c.getSimpleName().equals(sub)) {
                return c;
            }
        }
        throw new RuntimeException(String.format("Could not find subclass %s of %s", sub, parent));
    }

    /*public static ClassLoader getClassLoader() {
        return ESClassLoader.INSTANCE;
    }*/
}
