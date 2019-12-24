package com.yxy.nova.service.encryption;

import java.security.MessageDigest;

/**
 * @author yuxiaoyu
 * @Description:
 * @date 2019-07-11 14:51
 */
public class Md5Encrypt extends AbstractEncrypt {


    private Md5Encrypt(){
    }

    public static Md5Encrypt getInstance(){
        return SingletonHandler.singleton;
    }

    private static class SingletonHandler {
        private static Md5Encrypt singleton = new Md5Encrypt();
    }


    @Override
    public String encrypt(String text) {
        StringBuilder sb = new StringBuilder();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");

            md.update(text.getBytes("UTF-8"));
        } catch (Exception e) {
            return null;
        }
        byte[] b = md.digest();

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
