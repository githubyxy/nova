package test.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.tomcat.util.buf.HexUtils;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class RSAUtil {

    private static final String publicKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnCz5/ULWZv/DWrcx6/yRwP/tgEg80lJt0Qi+2EzFWAfYrjUfRAFTJdXCZx4kvJIrP9t5TrHIaE6NhVO29/1VThyQp5Mq6ZJ+peIOw0GRa5Li+9DguaNGkDo/7KHpTspTq+DjrQUhnOfz//Qyn04XZzMEYMf3C4SO5arqvoa/979KJ3BJcfwrBIkVJaoUP8Lb8gXtW5oMcaTryzfZAwplKJrt55jLAv5UpqtEtgrddY5jHoDQAZcrjZ1CmatsfXzSnN+j5t129YVMmcppJaDgqMdbAhLmAKRcZMd7AYr7MUbZvHZLF7TY23k0Dr7E3IW5vefGFRy/oNYNBTYJeaTqjwIDAQAB";
    private static final String privateKey="MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCcLPn9QtZm/8NatzHr/JHA/+2ASDzSUm3RCL7YTMVYB9iuNR9EAVMl1cJnHiS8kis/23lOschoTo2FU7b3/VVOHJCnkyrpkn6l4g7DQZFrkuL70OC5o0aQOj/soelOylOr4OOtBSGc5/P/9DKfThdnMwRgx/cLhI7lquq+hr/3v0oncElx/CsEiRUlqhQ/wtvyBe1bmgxxpOvLN9kDCmUomu3nmMsC/lSmq0S2Ct11jmMegNABlyuNnUKZq2x9fNKc36Pm3Xb1hUyZymkloOCox1sCEuYApFxkx3sBivsxRtm8dksXtNjbeTQOvsTchbm958YVHL+g1g0FNgl5pOqPAgMBAAECggEBAI50TBciVzUeSJA/l+68tCqKh5XCzo++FmNcic/8vulcRt4ooHtEhhZ109yBV1sWLRMTW6RXcAkM1fPud2/fCWe1kJD6IWNqd9FR0MtzzHSGSDsPvVw3PY8jQtnSmmB/FzEpciOJEa7wuNUYZvB+p8Q/edfZrlM4N6vwai0d9o7A41q8XG/D35qbWZPklLmrJDlWw01pepBPITR0/lL8N8n+H5KLwXXgRHfC6uKfKfE/3kvtQep7cgS1Jnlk3OxC6sX/6El/BVDDTiOMhPoVaMfWzq5l0F/tuAUsDdfEGynk4ETVb9ATvyzKL1uAtGyctmpN7HjWG50poQ1orK6OjgECgYEA4L85ETb44YQwGaSULIRsAtQN12Zbw3uzOI2Do5LhlSdrfxL8X8NRxdStl32UprYxy8zM6DYkmuG2LxDgqhVPjEvjXA4I7LNt58mUbmiWRukXcJMw9MVouqOu+sFISQ6Bqf2e+Snus3EYh4WD/6Ccnj3G6ZybPg1/1vJ3/eYj5Y8CgYEAseSuiC/63cL8oX2XJYfW1WXZky4ZEQDDTU/pepzLAJnSBZzEPuArEXo3ZIG2c8vUJEJpfXrfMhWnCkpepvFwNYVeoURTpMnvsRR2KZPC5V8PWQNwNdHufhLN6eUy7Z5xU7pPwJkDeDkIqAYRFMmdrkFgViT6iwnfqNV5g0/uKwECgYEA3kR/fcTuEqoWn+WiHPzBjXKGVdaHPiciMfXSkHz81/PqXfw618JlJs+U9lNeJhnd2X4r0CNrxDkhYh7A8bStvUR4EFxY+y7grPEw+Jg6ttpjMhR/wDNTxfGHZbqtcIMGPUX0aNrNlW1uIukqiC8hdWYHSJZ2+cGEAuI9RT7LlYUCgYEAhtApPGgwE9NrJO9KllSliL9Q7esnb+1GFNcUt6kPZvsrYLl3N7cmrG4UgISUwpibwobZ7xApCYnDN1c4+AdM5CKYnlprkRzAG1DIY6XpM26g8PJcHU1lEgIqLrFVsSI7/BnbyrerG3cNilhepYRNjPtaiK4peSkYaJivJ8ZxVgECgYAoRp3igm+a+Dtdf6EoyZnCeomehkZIEoykgsM9A2jm4w9oPC1kfJMjgHBpxoaCfYcedQgKzkPpS8Wjh7I+9GeNKCld3adrT4v23gCZfmbif8q5zeXLxlbhHQiSU49n91LTw4Pwn2jTYXzg88SmwNiTuicCCRg55NS6/z9KCn/axg==";


    public static void main(String[] args) throws Exception {
        // 生成RSA密钥对
//        KeyPair keyPair = generateKeyPair();
//
//        // 获取公钥和私钥
//        PublicKey publicKey = keyPair.getPublic();
//        PrivateKey privateKey = keyPair.getPrivate();
//
//        System.out.println(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
//        System.out.println(Base64.getEncoder().encodeToString(privateKey.getEncoded()));

        // 要加密的明文数据
        String plaintext = "hxkj@123";

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));

        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
        // 创建KeyFactory对象
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        // 生成私钥对象
        PrivateKey privateKey1 = keyFactory.generatePrivate(privateKeySpec);
        RSAPublicKey publicKey1 = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);

        // 使用公钥加密数据
        byte[] encryptedData = encrypt(plaintext, publicKey1);
        //加密后的数据：70743eb9df62fb8471b2023622eb47ce23a28b7a6ed8b04edc34a5b49591818686b23d9a5e0c8a269deb08bfd2a1a2169893508d191eb4453886576f06c7829183b51ee699ea750a35d606835ee08e3afedb977c265d0317344076f12074ea9cfc794abc5fc7a73797fc69f9e9903183d8d16bd23d33f356658e9925f72d7594c33374db3926c3518e8cf3a23dbedeae17c6b960943662e733825930eac918fbc60ca139730bd511d515fb1ec85371f91d90ef3bcb279974ddd053745ff94fad46292392ad6c8f2918e0b4a480d7ba4c93c8a05a4d55d4a259e85198a6e6a35217609a38fa9d070b1ec1f1dd6a5af56bafae5c17d3393b52de0dd92a297414cd
        System.out.println("加密后的数据：" + Hex.encodeHexString(encryptedData));
//        System.out.println("加密后的数据：" + HexUtils.toHexString(encryptedData));
//        System.out.println("加密后的数据：" + bytesToHex(encryptedData));

        // 使用私钥解密数据
        String decryptedText = decrypt(encryptedData, privateKey1);
        // 解密后的数据：Hello, RSA encryption!
        System.out.println("解密后的数据：" + decryptedText);
    }

    // 生成RSA密钥对
    private static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    // 使用公钥加密数据
    private static byte[] encrypt(String plaintext, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
    }

    // 使用私钥解密数据
    private static String decrypt(byte[] encryptedData, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedData = cipher.doFinal(encryptedData);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    // 将字节数组转换为十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
