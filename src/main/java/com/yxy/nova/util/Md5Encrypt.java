package com.yxy.nova.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author yuxiaoyu
 * @Description:
 * @date 2019-07-11 14:51
 */
public class Md5Encrypt {

    public static String encrypt(String text) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");

            md.update(text.getBytes("UTF-8"));
        } catch (Exception e) {
            return null;
        }
        byte[] b = md.digest();
        return bytes2HexString(b);
    }

    public static String encrypt(String data, String salt) {
        try {
            return bytes2HexString(md5ToByte((data + salt).getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    static byte[] md5ToByte(byte[] data) {
        return hashTemplate(data, "MD5");
    }

    private static byte[] hashTemplate(byte[] data, String algorithm) {
        if (data != null && data.length > 0) {
            try {
                MessageDigest md = MessageDigest.getInstance(algorithm);
                md.update(data);
                return md.digest();
            } catch (NoSuchAlgorithmException var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static String bytes2HexString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int offset = 0; offset < b.length; ++offset) {
            int i = b[offset];
            if (i < 0) {
                i += 256;
            }

            if (i < 16) {
                sb.append("0");
            }

            sb.append(Integer.toHexString(i));
        }
        return sb.toString();
    }

}
