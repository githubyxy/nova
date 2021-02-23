package com.yxy.nova.mwh.elasticsearch.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonUtil {

    public static String md5(String x) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("failed to get md5 instance", ex);
        }
        md.update(x.getBytes(Constants.UTF8));
        byte[] b = md.digest();
        StringBuilder sb = new StringBuilder();
        for (int offset = 0; offset < b.length; ++offset) {
            int i = b[offset];
            if (i < 0) i += 256;
            if (i < 16) sb.append("0");
            sb.append(Integer.toHexString(i));
        }
        return sb.toString();
    }

}
