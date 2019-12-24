package com.yxy.nova.service.encryption;

import java.security.MessageDigest;

/**
 * @author yuxiaoyu
 * @Description:
 * @date 2019-07-11 14:51
 */
public class SHA256Encrypt extends AbstractEncrypt {

    private SHA256Encrypt(){
    }

    public static SHA256Encrypt getInstance(){
        return SHA256Encrypt.SingletonHandler.singleton;
    }

    private static class SingletonHandler {
        private static SHA256Encrypt singleton = new SHA256Encrypt();
    }

    @Override
    public String encrypt(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            messageDigest.update(text.getBytes("UTF-8"));

            return byte2Hex(messageDigest.digest());

        } catch (Exception e) {
            return null;
        }
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }


}
