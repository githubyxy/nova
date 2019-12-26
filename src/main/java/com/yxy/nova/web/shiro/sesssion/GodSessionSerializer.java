package com.yxy.nova.web.shiro.sesssion;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: yuxiaoyu
 * @date: 2018-04-18 下午6:43
 */
public class GodSessionSerializer implements RedisSerializer<Object> {

    @Override
    public byte[] serialize(Object x) throws SerializationException {
        try {
            if (x == null) {
                return null;
            }
            return encodeSession((Serializable) x).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return new byte[0];
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        try {
            if (bytes == null) {
                return null;
            }
            return decodeSession(new String(bytes, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private Object decodeSession(String session) {
        if (session == null) {
            return null;
        }
        byte[] sessionBytes = base64Decode(session);
        return javaDeserialize(sessionBytes);
    }

    private String encodeSession(Serializable x) {
        if (x == null) {
            return null;
        }
        byte[] sessionBytes = javaSerialize(x);
        return base64Encode(sessionBytes);
    }

    private byte[] base64Decode(String session) {
        return Base64.decodeBase64(session);
    }

    private Object javaDeserialize(byte[] bytes) {
        return SerializationUtils.deserialize(bytes);
    }

    private byte[] javaSerialize(Serializable x) {
        return SerializationUtils.serialize(x);
    }

    private String base64Encode(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

}
