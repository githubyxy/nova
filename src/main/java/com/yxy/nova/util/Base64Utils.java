package com.yxy.nova.util;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by yuxiaoyu on 19/5/27.
 */
public class Base64Utils {

    public static final String encode(String source) {
        String base64Str;

        try {
            base64Str = Base64.encodeBase64String(source.getBytes("UTF-8"));
            return base64Str;
        } catch (UnsupportedEncodingException var3) {
            throw new RuntimeException("", var3);
        }
    }

    public static final String decode(String base64Str) {
        String source;

        try {
            source = new String(Base64.decodeBase64(base64Str.getBytes("UTF-8")), "UTF-8");
            return source;
        } catch (UnsupportedEncodingException var3) {
            throw new RuntimeException("", var3);
        }
    }
}
