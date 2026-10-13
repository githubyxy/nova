package test.yxy;

import groovy.util.logging.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class VAESTest {

    private static final String SECRET_KEY = "lwIr26BpMdvWmFLg";
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String encryptAES(String key, String value) {
        try {
            byte[] keyBytes = Arrays.copyOf(key.getBytes(StandardCharsets.UTF_8), 16);
            SecretKey keyStr = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keyStr);
            byte[] cleartext = value.getBytes(StandardCharsets.UTF_8);
            byte[] ciphertextBytes = cipher.doFinal(cleartext);
            return bytesToHex(ciphertextBytes);
        } catch (Exception e) {
        }
        return null;
    }

    public static String encrypt(String value) {
        return encryptAES(SECRET_KEY, value);
    }

    public static String decryptAES(String key, String encrypted) {
        try {
            byte[] keyBytes = Arrays.copyOf(key.getBytes(StandardCharsets.UTF_8), 16);
            SecretKey keyStr = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keyStr);
            int len = encrypted.length();
            byte[] content = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                content[i / 2] = (byte) ((Character.digit(encrypted.charAt(i), 16) << 4)
                        + Character.digit(encrypted.charAt(i + 1), 16));
            }
            byte[] ciphertextBytes = cipher.doFinal(content);
            return new String(ciphertextBytes);
        } catch (Exception e) {
        }
        return null;
    }

    public static String decrypt(String encrypted) {
        return decryptAES(SECRET_KEY, encrypted);
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static void main(String[] args) {
        System.out.println(encrypt("13112344321"));
        System.out.println(decrypt(encrypt("13112344321")));
    }
}
