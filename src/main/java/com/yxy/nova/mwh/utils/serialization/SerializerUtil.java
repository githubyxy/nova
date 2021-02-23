package com.yxy.nova.mwh.utils.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 序列化反序列化工具类
 * Created by chenchanglong on 2019/8/16.
 */
public class SerializerUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    /**
     * json序列化——jackson
     * @param obj
     * @return
     * @throws JsonProcessingException
     */
    @SneakyThrows
    public static final String jsonSerialize(Object obj) {
        if (obj == null) {
            return null;
        }
        return mapper.writeValueAsString(obj);
    }

    /**
     * json反序列化——jackson
     * @param content
     * @param clazzType
     * @param <T>
     * @return
     * @throws IOException
     */
    @SneakyThrows
    public static final <T> T jsonDeserialize(String content, Class<T> clazzType) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        return mapper.readValue(content, clazzType);
    }

    /**
     * json反序列化——jackson
     * @param content
     * @param typeReference
     * @param <T>
     * @return
     * @throws IOException
     */
    @SneakyThrows
    public static final <T> T jsonDeserialize(String content, TypeReference typeReference) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        return mapper.readValue(content, typeReference);
    }

    /**
     * json列表反序列化-jackson
     * @param content
     * @param clazzType
     * @param <T>
     * @return
     * @throws IOException
     */
    @SneakyThrows
    public static final <T> List<T> jsonListDeserialize(String content, Class<T> clazzType) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, clazzType);
        return mapper.readValue(content, javaType);
    }

    /**
     * json列表反序列化, 如果结果为NUll, 返回Empty List, 保证返回结果不为NULL
     * @param content
     * @param clazzType
     * @param <T>
     * @return
     */
    @SneakyThrows
    public static final <T> List<T> jsonListDeserializeNullAsEmptyList(String content, Class<T> clazzType) {
        List<T> result = jsonListDeserialize(content, clazzType);
        return result != null ? result : new ArrayList<>();
    }

    /**
     * json Map反序列化-jackson, 如果结果为NUll, 返回Empty Map, 保证返回结果不为NULL
     * @param content
     * @param keyClassType
     * @param valueClassType
     * @return
     * @throws IOException
     */
    @SneakyThrows
    public static final <K, V> Map<K, V> jsonMapDeserializeNullAsEmptyMap(String content, Class<K> keyClassType, Class<V> valueClassType) {
        Map<K, V> result = jsonMapDeserialize(content, keyClassType, valueClassType);
        return result != null ? result : new HashMap<>();
    }

    /**
     * json Map反序列化-jackson
     * @param content
     * @param keyClassType
     * @param valueClassType
     * @return
     * @throws IOException
     */
    @SneakyThrows
    public static final <K, V> Map<K, V> jsonMapDeserialize(String content, Class<K> keyClassType, Class<V> valueClassType) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        JavaType javaType = mapper.getTypeFactory().constructMapType(Map.class, keyClassType, valueClassType);
        return mapper.readValue(content, javaType);
    }

    /**
     * java 原生序列化
     * @param obj
     * @return
     * @throws IOException
     */
    @SneakyThrows
    public static final byte[] serialize(Object obj) {
        if(null == obj) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        }finally {
            if(null != byteArrayOutputStream) {
                byteArrayOutputStream.close();
            }

            if(null != objectOutputStream) {
                objectOutputStream.close();
            }
        }
    }

    /**
     * java 原生反序列化
     * @param bytes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SneakyThrows
    public static final Object deserialize(byte[] bytes) {
        if(null == bytes) {
            return null;
        }

        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object obj = objectInputStream.readObject();
            return obj;
        }finally {
            if(null != objectInputStream) {
                objectInputStream.close();
            }

            if(null != byteArrayInputStream) {
                byteArrayInputStream.close();
            }
        }
    }
}
