package test.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

@Slf4j
public class DidiSignUtil {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5PADDING";
    private static final int IV_SIZE = 16;

    @SneakyThrows
    public static String bodyEncryptByAccessKey(String body, String accessKey, Long timestamp){
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(accessKey.getBytes(StandardCharsets.UTF_8)), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        initCipher(timestamp, cipher, Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(body.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    @SneakyThrows
    public static String bodyDecryptByAccessKey(String encryptBody, String accessKey, Long timestamp){
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(accessKey.getBytes(StandardCharsets.UTF_8)), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        initCipher(timestamp, cipher, Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptBody));
        String decryptedBody = new String(decryptedBytes, StandardCharsets.UTF_8);
        log.info("加密body体根据accessKey解密后参数信息||decryptedBody={}", decryptedBody);
        return decryptedBody;
    }

    /**
     * hermes 请求滴滴时 生成签名
     *
     * @param encryptedBody
     * @param supplierCode
     * @param secretKey
     * @param timestamp
     * @return
     * @throws Exception
     */
    public static String generateOuterSign(String encryptedBody, String supplierCode, String secretKey, Long timestamp){
        String toBeSignedString = encryptedBody + supplierCode + timestamp + secretKey;
        String sign = Base64.getEncoder().encodeToString(hmacSHA256(toBeSignedString, secretKey).getBytes());
        return sign;
    }

    /**
     * 滴滴请求hermes 生成签名，用于hermes系统验签
     *
     * @param encryptedBody
     * @param secretKey
     * @param timestamp
     * @return
     * @throws Exception
     */
    public static String generateRequestSupplierSign(String encryptedBody, String secretKey, Long timestamp){
        String toBeSignedString = encryptedBody + timestamp + secretKey;
        String sign = Base64.getEncoder().encodeToString(hmacSHA256(toBeSignedString, secretKey).getBytes());
        return sign;
    }

    private static void initCipher(Long timestamp, Cipher cipher, int mode, SecretKeySpec secretKey) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (Objects.nonNull(timestamp)) {
            byte[] ivBytes = genTimestampIV(timestamp).getBytes(StandardCharsets.UTF_8);
            cipher.init(mode, secretKey, new IvParameterSpec(ivBytes));
        } else {
            cipher.init(mode, secretKey);
        }
    }

    @SneakyThrows
    private static String hmacSHA256(String data, String signKey){
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(signKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signature = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return byteArrayToHexString(signature);
    }

    private static String genTimestampIV(long timestamp) {
        String str = StringUtils.reverse(String.valueOf(timestamp));
        if (str.length() > IV_SIZE) {
            return str.substring(0, IV_SIZE);
        } else if (str.length() < IV_SIZE) {
            StringBuilder sb = new StringBuilder();
            while (sb.length() < IV_SIZE - str.length()) {
                sb.append('0');
            }
            sb.append(str);
            return sb.toString();
        } else {
            return str;
        }
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    public static String generateSignature(String caller, String token, String timestamp) {
        try {
            String input = caller + token + timestamp;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256算法不可用", e);
        }
    }

    public static boolean verifySignature(String encryptedBody, String secretKey, Long timestamp, String signature) {
        return signature.equals(generateRequestSupplierSign(encryptedBody, secretKey, timestamp));
    }

}