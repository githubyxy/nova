package com.yxy.nova.util;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AESUtil {
        private static final String AES_ALGORITHM = "AES";

        @SneakyThrows
        public static String encrypt(String plaintext, String key) {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            return Base64.getEncoder().encodeToString(cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8)));
        }

        @SneakyThrows
        public static String decrypt(byte[] ciphertext, String key) {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());

            cipher.init(Cipher.DECRYPT_MODE, secretKey,iv);
            byte[] decryptedBytes = cipher.doFinal(ciphertext);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        }

    public static void main(String[] args) {
            // d5i0e4LLVcWE+PULz/FzJQ==

        aaa("张水");
        aaa("18221302599");
        aaa("18221302600");
        aaa("18221302601");
        aaa("18221302602");
        aaa("18221302603");



//        System.out.println(decrypt(Base64.getDecoder().decode(encrypt), "7fd2e257a128435b8b6574e5753d825d"));
    }

    private static void aaa(String text) {
        String encrypt = encrypt(text, "fC8tzaLDItGjIjOr");
        System.out.println(encrypt);
    }
}
