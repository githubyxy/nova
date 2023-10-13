package com.yxy.nova.util;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AES_ECBUtil {
        private static final String AES_ALGORITHM = "AES";

        @SneakyThrows
        public static byte[] encrypt(String plaintext, String key) {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] bytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            return bytes;
        }

        @SneakyThrows
        public static String decrypt(byte[] ciphertext, String key) {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(ciphertext);
            return new String(decryptedBytes);
        }

    @SneakyThrows
    public static void main(String[] args) {
            // d5i0e4LLVcWE+PULz/FzJQ==
        byte[] encrypt = encrypt("{\n" +
                "    \"timestamp\":1696838346574,\n" +
                "    \"deviceId\":\"coap_test1\",\n" +
                "    \"properties\":{\"temp\":42}\n" +
                "}", "hxkjhxkjhxkjhxkj");

        File file = new File("/Users/yuxiaoyu/Downloads/1.bin");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(encrypt);

        System.out.println(new String(encrypt, StandardCharsets.UTF_8));

        // coap-client -k hxkjhxkjhxkjhxkj -m post -f /tmp/1.bin coap://127.0.0.1:61890/coap_1/coap_test1/properties/report

//        System.out.println(decrypt(Base64.getDecoder().decode(encrypt), "hxkjhxkjhxkjhxkj"));
        System.out.println(decrypt(encrypt, "hxkjhxkjhxkjhxkj"));
    }
}
