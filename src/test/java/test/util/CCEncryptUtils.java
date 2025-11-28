package test.util;

import com.yxy.nova.mwh.utils.MD5Util;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class CCEncryptUtils {

    public static void main(String[] args) {

        String client_id ="yywl";
        String s  = "13585934620";

        String aes_key = MD5Util.md5("yywlaes");
        System.out.println(aes_key);
        // 加密数据
        String aesPhone = aesEncrypt(aes_key, "13585934620");
        System.out.println(aesPhone);
        String pname = aesEncrypt(aes_key, "于晓宇");
        System.out.println(pname);
        String code = aesEncrypt(aes_key, "340322199203217415");
        System.out.println(code);

    }

    public static String string2MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString().toLowerCase();
    }

    /**
     * 解密
     *
     * @param keyStr 密钥
     * @param value  解密数据
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws UnsupportedEncodingException
     */
    public static String aesDecrypt(String MD5Str, String encrypted) {
        try {
//            String MD5Str = string2MD5(keyStr);// 调整为32 位16 进制
            byte[] keyBytes = Hex.decodeHex(MD5Str.toCharArray());//转换为128 位二进制数据
            Key key = new SecretKeySpec(keyBytes, "AES");// Key 转换
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 解密
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodeResult =
                    cipher.doFinal(Hex.decodeHex(encrypted.toCharArray()));
            return new String(decodeResult, "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 加密
     *
     * @param keyStr 密钥
     * @param srcStr 加密数据
     * @return
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws UnsupportedEncodingException
     */
    public static String aesEncrypt(String MD5Str, String srcStr) {
        try {
//            String MD5Str = string2MD5(keyStr);// 调整为32 位16 进制
            byte[] keyBytes = Hex.decodeHex(MD5Str.toCharArray());//转换为128 位二进制数据
            Key key = new SecretKeySpec(keyBytes, "AES");// Key 转换
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 加密
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encodeResult =
                    cipher.doFinal(srcStr.getBytes("UTF-8"));
            return Hex.encodeHexString(encodeResult);
        } catch (Exception e) {
            return null;
        }
    }
}
