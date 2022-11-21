package com.yxy.nova.mwh.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by qi.wang on 17/7/14.
 */
public class MD5Util {
    public static String md5(String src) {
        if(StringUtils.isNotBlank(src)) {
            try {
                MessageDigest m = MessageDigest.getInstance("MD5");
                m.reset();
                m.update(src.getBytes("utf-8"));
                byte[] digest = m.digest();
                BigInteger bigInt = new BigInteger(1, digest);

                String hashtext;
                for(hashtext = bigInt.toString(16); hashtext.length() < 32; hashtext = "0" + hashtext) {
                    ;
                }

                return hashtext;
            } catch (Exception var5) {
                ;
            }
        }

        return null;
    }
}
