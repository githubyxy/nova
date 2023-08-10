package com.yxy.nova.util;

import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtil32 {
    public static final String secretKey = "7fd2e257a128435b8b6574e5753d825d";
    private static final String KEY_CHARSET = "UTF-8";
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";
    private static final Integer PRIVATE_KEY_SIZE_BIT = 256;
    private static final Integer PRIVATE_KEY_SIZE_BYTE = 32;
    private static final String VI = "zGrVju3LPhyhiJR8";

    public AESUtil32() {
    }

    public static String encrypt(String plainText) {
        return encrypt("7fd2e257a128435b8b6574e5753d825d", plainText);
    }

    public static String encrypt(String secretKey, String plainText) {
        if (StringUtils.isBlank(secretKey)) {
            secretKey = "7fd2e257a128435b8b6574e5753d825d";
        }

        if (secretKey.length() != PRIVATE_KEY_SIZE_BYTE) {
            throw new RuntimeException("AESUtil:Invalid AES secretKey length (must be 16 bytes)");
        } else {
            String cipherText = "";

            try {
                Cipher cipher = initParam(secretKey, 1);
                byte[] bytePlainText = plainText.getBytes("UTF-8");
                byte[] byteCipherText = cipher.doFinal(bytePlainText);
                cipherText = Base64.getEncoder().encodeToString(byteCipherText);
                return cipherText;
            } catch (Exception var6) {
                throw new RuntimeException("AESUtil:encrypt fail!", var6);
            }
        }
    }

    public static String decrypt(String cipherText) {
        return decrypt("7fd2e257a128435b8b6574e5753d825d", cipherText);
    }

    public static String decrypt(String secretKey, String cipherText) {
        if (StringUtils.isBlank(secretKey)) {
            secretKey = "7fd2e257a128435b8b6574e5753d825d";
        }

        if (secretKey.length() != PRIVATE_KEY_SIZE_BYTE) {
            throw new RuntimeException("AESUtil:Invalid AES secretKey length (must be 16 bytes)");
        } else {
            String plainText = "";

            try {
                Cipher cipher = initParam(secretKey, 2);
                byte[] byteCipherText = Base64.getDecoder().decode(cipherText);
                byte[] bytePlainText = cipher.doFinal(byteCipherText);
                plainText = new String(bytePlainText, "UTF-8");
                return plainText;
            } catch (Exception var6) {
                throw new RuntimeException("AESUtil:decrypt fail!", var6);
            }
        }
    }

    public static Cipher initParam(String secretKey, int mode) {
        try {
//            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
//            secureRandom.setSeed(secretKey.getBytes());
//            KeyGenerator keygen = KeyGenerator.getInstance("AES");
//            keygen.init(PRIVATE_KEY_SIZE_BIT, secureRandom);
            byte[] raw = secretKey.getBytes();
            SecretKeySpec key = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("zGrVju3LPhyhiJR8".getBytes());
            cipher.init(mode, key, iv);
            return cipher;
        } catch (Exception var8) {
            throw new RuntimeException("AESUtil:initParam fail!", var8);
        }
    }

    public static void main(String[] args) {
        long s = System.currentTimeMillis();
        String text = "于晓宇";
        String encryptMsg = encrypt("7fd2e257a128435b8b6574e5753d825d", text);
        System.out.println("密文为：" + encryptMsg);
        long e = System.currentTimeMillis();
        System.out.println(e - s);
        String decryptMsg = decrypt("7fd2e257a128435b8b6574e5753d825d", encryptMsg);
        System.out.println("明文为：" + decryptMsg);
        long d = System.currentTimeMillis();
        System.out.println(d - e);
    }
}

