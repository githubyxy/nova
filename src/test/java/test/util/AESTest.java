package test.util;

import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @author yuxiaoyu
 * @description:
 * @date 2024/12/19
 */
public class AESTest {

    private static final String key = "icekredit2sykey0";

    private static String aesEncrypt(String key, String value) throws Exception {
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        c.init(Cipher.ENCRYPT_MODE, keySpec);
        return new String(Base64.getEncoder().encode(c.doFinal(value.getBytes())));
    }

    private static String aesDecrypt(String key, String encrypted) throws Exception {
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        c.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decodedValue = Base64.getDecoder().decode(encrypted);
        byte[] decryptedValue = c.doFinal(decodedValue);
        return new String(decryptedValue);
    }

    @Test
    public void test() throws Exception {
        String mobile = "12121212121";
        String s = aesEncrypt(key, mobile);
        System.out.println(s);
    }

    @Test
    public void test2() throws Exception {
        String s = "qDhUxv/QJMFci/FjNpzCFA==";
        String s1 = aesDecrypt(key, s);
        System.out.println(s1);
    }

}
