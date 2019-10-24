package com.yxy.nova.conf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Arrays;


/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: yuxiaoyu
 * @date: 2018-04-27 下午5:44
 */
public class CustomizedFastJsonHttpMessageConverter extends FastJsonHttpMessageConverter {

    private static final SerializerFeature[] openApiFeatures = new SerializerFeature[] {
            // 是否输出为null的字段,若为null 则显示该字段
            SerializerFeature.WriteMapNullValue,
            // List字段如果为null,输出为[],而非null
            SerializerFeature.WriteNullListAsEmpty,
            // 循环引用
            SerializerFeature.DisableCircularReferenceDetect
    };

    private static final SerializerFeature[] hertzWebFeatures = new SerializerFeature[] {
            // 是否输出为null的字段,若为null 则显示该字段
            SerializerFeature.WriteNullStringAsEmpty,
            // List字段如果为null,输出为[],而非null
            SerializerFeature.WriteNullListAsEmpty,
            // 循环引用
            SerializerFeature.DisableCircularReferenceDetect};

    private static final String UTF_8 = "UTF-8";
    private static final Charset DEFAULT_CHARSET = Charset.forName(UTF_8);

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        Object object = super.read(type, contextClass, inputMessage);
        object = trim(object);
        return object;
    }

    @Override
    public void write(Object o, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        if(o instanceof Object){
            outputMessage.getHeaders().setAcceptCharset(Arrays.asList(DEFAULT_CHARSET));
            outputMessage.getHeaders().set("Content-Type", "application/json;charset=UTF-8");
            IOUtils.write(JSON.toJSONString(o, openApiFeatures), outputMessage.getBody(), UTF_8);
        } else {
            super.write(o, type, contentType, outputMessage);
        }
    }

    /**
     * 对参数对象中的string字段去空格
     * @param object
     * @return
     */
    private Object trim(Object object) {
        if (object == null) {
            return object;
        }

        Class<?> parameterType = object.getClass();

        if (String.class.equals(parameterType)) {
            return StringUtils.trimToEmpty((String)object);
        } else {
            return object;
        }
    }
}
