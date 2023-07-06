package test.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Hex;
import org.apache.tomcat.util.buf.HexUtils;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RASUtilTest {

    private final String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnCz5/ULWZv/DWrcx6/yRwP/tgEg80lJt0Qi+2EzFWAfYrjUfRAFTJdXCZx4kvJIrP9t5TrHIaE6NhVO29/1VThyQp5Mq6ZJ+peIOw0GRa5Li+9DguaNGkDo/7KHpTspTq+DjrQUhnOfz//Qyn04XZzMEYMf3C4SO5arqvoa/979KJ3BJcfwrBIkVJaoUP8Lb8gXtW5oMcaTryzfZAwplKJrt55jLAv5UpqtEtgrddY5jHoDQAZcrjZ1CmatsfXzSnN+j5t129YVMmcppJaDgqMdbAhLmAKRcZMd7AYr7MUbZvHZLF7TY23k0Dr7E3IW5vefGFRy/oNYNBTYJeaTqjwIDAQAB";
    private final String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCcLPn9QtZm/8NatzHr/JHA/+2ASDzSUm3RCL7YTMVYB9iuNR9EAVMl1cJnHiS8kis/23lOschoTo2FU7b3/VVOHJCnkyrpkn6l4g7DQZFrkuL70OC5o0aQOj/soelOylOr4OOtBSGc5/P/9DKfThdnMwRgx/cLhI7lquq+hr/3v0oncElx/CsEiRUlqhQ/wtvyBe1bmgxxpOvLN9kDCmUomu3nmMsC/lSmq0S2Ct11jmMegNABlyuNnUKZq2x9fNKc36Pm3Xb1hUyZymkloOCox1sCEuYApFxkx3sBivsxRtm8dksXtNjbeTQOvsTchbm958YVHL+g1g0FNgl5pOqPAgMBAAECggEBAI50TBciVzUeSJA/l+68tCqKh5XCzo++FmNcic/8vulcRt4ooHtEhhZ109yBV1sWLRMTW6RXcAkM1fPud2/fCWe1kJD6IWNqd9FR0MtzzHSGSDsPvVw3PY8jQtnSmmB/FzEpciOJEa7wuNUYZvB+p8Q/edfZrlM4N6vwai0d9o7A41q8XG/D35qbWZPklLmrJDlWw01pepBPITR0/lL8N8n+H5KLwXXgRHfC6uKfKfE/3kvtQep7cgS1Jnlk3OxC6sX/6El/BVDDTiOMhPoVaMfWzq5l0F/tuAUsDdfEGynk4ETVb9ATvyzKL1uAtGyctmpN7HjWG50poQ1orK6OjgECgYEA4L85ETb44YQwGaSULIRsAtQN12Zbw3uzOI2Do5LhlSdrfxL8X8NRxdStl32UprYxy8zM6DYkmuG2LxDgqhVPjEvjXA4I7LNt58mUbmiWRukXcJMw9MVouqOu+sFISQ6Bqf2e+Snus3EYh4WD/6Ccnj3G6ZybPg1/1vJ3/eYj5Y8CgYEAseSuiC/63cL8oX2XJYfW1WXZky4ZEQDDTU/pepzLAJnSBZzEPuArEXo3ZIG2c8vUJEJpfXrfMhWnCkpepvFwNYVeoURTpMnvsRR2KZPC5V8PWQNwNdHufhLN6eUy7Z5xU7pPwJkDeDkIqAYRFMmdrkFgViT6iwnfqNV5g0/uKwECgYEA3kR/fcTuEqoWn+WiHPzBjXKGVdaHPiciMfXSkHz81/PqXfw618JlJs+U9lNeJhnd2X4r0CNrxDkhYh7A8bStvUR4EFxY+y7grPEw+Jg6ttpjMhR/wDNTxfGHZbqtcIMGPUX0aNrNlW1uIukqiC8hdWYHSJZ2+cGEAuI9RT7LlYUCgYEAhtApPGgwE9NrJO9KllSliL9Q7esnb+1GFNcUt6kPZvsrYLl3N7cmrG4UgISUwpibwobZ7xApCYnDN1c4+AdM5CKYnlprkRzAG1DIY6XpM26g8PJcHU1lEgIqLrFVsSI7/BnbyrerG3cNilhepYRNjPtaiK4peSkYaJivJ8ZxVgECgYAoRp3igm+a+Dtdf6EoyZnCeomehkZIEoykgsM9A2jm4w9oPC1kfJMjgHBpxoaCfYcedQgKzkPpS8Wjh7I+9GeNKCld3adrT4v23gCZfmbif8q5zeXLxlbhHQiSU49n91LTw4Pwn2jTYXzg88SmwNiTuicCCRg55NS6/z9KCn/axg==";
//private final String privateKey = "-----BEGIN RSA PRIVATE KEY-----MIIEowIBAAKCAQEAvnuBHhh9/Mwi4Z8Ea2IfVc7SJYyKkJT+4m4LBJbgxRVPa+ej1iAvJAJ++1CNfH6oVr6tw/wqEIZybTG7+iPm7SzfoW9eb/LYsLR43zLqriYWy7qAd7S/13zILiAGLUy/h9CDYwoXYaNXanVGe0iJb+NXmg7f4MAEo7rR15QHQ8zDtomPI/Fz9vz+o/xzHqsLVnxZIpn4dlCp+7QtzOePnvEda5xqWk4DNaVV2fr9gwfBFd8MyAP+CyBXLAWpmxinRU6knsLKAlqcbOLNjk2+aPNvH9EWHODKWBuMPpdDwQexD+V3/nOp8c07NzSfwXmlZuRKod9SqVY88Pg1r84WwQIDAQABAoIBAD/Fb2vY/U0GNuNcbolY926t6FMj4zwWdeLuvoeRz7yLruRMT+CfM8kVgNe4lzXH3an0+o3zvU19BZAjJ3DOVkCvVktg0rgeEIfQU+s9y630GIuqa9onzFZjc01l8Ut3N2RL3cDpnvxUSC7fBnK9zS2Fak2sjy1ae/Mo15oZ9/itX1WYaVmDmnqopbbopfEL5UkqgwTe8KlZs7iTr5wqBqOfaTS/QU8pEXBy0m3HQwmH2KUfK9ZZOXJGPF+WZ+2OU95mFEscLNwXRZcrhO0SbcLh6dZiNZJBdu3dANsOPsPO5DUhZskLHJMeiFK3yOUcpgK6R51tTLxGYvJBHKd3nlUCgYEAxfLvsCinuORa3oUhWNaKRwa14uTAyhN1Gd6w/jGOcdM7x5vKqahD6iEMay+7B//3xNYjiPVZlyVojBxuvjDMuW9/jg3nWL7taf5WJTVQUNzE/h0vShPFQy6zskgeXYp/ceHNZzkoY3OqnW/qDrDvrLUZJLL7DM/PUXw7AJ5TH18CgYEA9lgEPuP8/h+tH6uxH8rdr4TmgsGrhProAvFw0CTSFsOlOWM2IP7U3/MYsMlWCVB8CZXgfX4z6FsWKlFT4gXwq96VaCfRyyvVQLsxZ10RyjhaZZKQS8p8n8DaRIjRh2Otqi8RJcdoNzGYGrxzZXO+H9vCOudDFRJ8skDyGQwoHd8CgYAMLzH1w+rfBJ9bRxKQZ1RHnvAfMTl0ttHkb8KxQtejxf7OAJbu7n6PsM0U4/E9QtI7eXrieUe0eAcBaNagOly/pU9SrSRqYAY5GgCvdaKl95Dgm2dHxgPEaOGCvdGwrE+/8Mg17FzCEmKUIklj8hwuo08S5Tlyxm/aci0nkpdHPQKBgQCEETXdfZ8BonvPEd34vGrTEX3/NdM9jMJb6ZLglAzp1WO7+3tmfxgjJBHlnVr5zWNiRC7BOKqddE/Syh0dfK/FhKQ23bo0oUBH3tOipZvG/SVBdSzU5S6g+uRJf8xNOFw/c9JOnbQ6euUf2E5JYp7YN5nOuHMXVfzyF61zTjgnowKBgHqozPZH2yTU0bgQfZBsUBFB4vpUkfzB6o6qOOFQ4uiLqvW64LghE1yf7S5+XYz8ZXtIf7yb7sZcJE6XR9BD4gdYl/0KnWRL+oJQMUGDZDBzOcHiHQ8gdyACAC5hFexngJE0RNHtYcg2QjUNMfZMu/eUXJ1EjzisCT0xbwIXv0oZ-----END RSA PRIVATE KEY-----";




    @Test
    public void test() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        // 加载PKCS1格式的私钥
        // 将PKCS#1私钥转换为PKCS#8私钥
        byte[] privateKeyPkcs8Bytes = convertToPkcs8(Base64.getDecoder().decode(privateKey));

        // 创建PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyPkcs8Bytes);

        // 创建KeyFactory对象
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        // 生成私钥对象
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        // 创建Cipher对象并指定算法
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        // 初始化Cipher为解密模式，并传入私钥
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        // 加密后的数据（密文）
        String decry = "YsDX23D9booXYAQXAv5sLeEPF/vOExDAOXptTGvmyPoyNGmL/cA4rnHk6FtZC0SO8ZUrxAkl2RqOlFAbYQ3yBAJipZ4FGYWYhWMhbYxYY2ylNsgWoQstxorOrP7Q/wGZPlYzCeVjro6M+hYxnYUeRh9PJ90uajuTaiwqBEt2mEHR3lfg76JwN/A5qBOTtF9lTem8+O7NGs4w+FmvFCg/deOKOBsrZzJ9wOg/TbbqYy+cTiW5HpnA05Iq1CRWOfPY1a8yFXONChPh/ODfsmCKmhb/uUrfTd4mGYNOlzBD/6YxkOWLGlltX5xsqZZa2QnlXx6IKLed4bfv1ZhVwZL/Rg==";
        byte[] encryptedData = Base64.getDecoder().decode(decry);

        // 解密数据
        byte[] decryptedData = cipher.doFinal(encryptedData);

        // 将解密后的字节数组转换为字符串
        String decryptedText = new String(decryptedData, StandardCharsets.UTF_8);

        System.out.println("解密结果: " + decryptedText);
    }

    private static byte[] convertToPkcs8(byte[] privateKeyBytes) throws IOException {
        // 添加PKCS#8私钥头部
        String pkcs8Header = "-----BEGIN PRIVATE KEY-----\n";
        String pkcs8Footer = "\n-----END PRIVATE KEY-----\n";
        String privateKeyPem = new String(privateKeyBytes);
        String pkcs8Pem = pkcs8Header + privateKeyPem + pkcs8Footer;
        return pkcs8Pem.getBytes();
    }


    @Test
    public void test2() throws Exception {
        // 要加密的明文数据
//        String plaintext = "Hello, RSA encryption!";

        // 加载PKCS1格式的私钥
        // 将PKCS#1私钥转换为PKCS#8私钥
//        byte[] privateKeyPkcs8Bytes = convertToPkcs8(Base64.getDecoder().decode(privateKey));

        // 创建PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));

        // 创建KeyFactory对象
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        // 生成私钥对象
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
//        keyFactory.generatePublic()

        // 使用公钥加密数据
//        byte[] encryptedData = encrypt(plaintext, publicKey);
//        System.out.println("加密后的数据：" + bytesToHex(encryptedData));

        // 使用私钥解密数据
//        String data ="NRQvl3Rti8UPeL7+Ani134wT1qSG5vqdSnZP2ZLeVp8gOmMrFFes6IPZwmALfrB7ZsQUxzD/Rry0YH86ss5Lk1r078X93Og5TJIHtuOWEsDftqOeEW458FpyodC7tQmYk9kQqFUJ5X/GPx4idTtU5i4WxfKvCv7t4imcKtD2RlX5vz1CIa/+XeapkDTiOMUFhNSTuwKlGZtczVMBcHq8gBtZXUzSDAxlvn5PviXeLQy6LTBOwzujCwDxIMH1EJRfI8Cq+b7A3RdBEqfZrXjZgm6AptDIT/Gjljdvmu0lujo4IfZ9O5kBCdqkAX6X0CqJSzA5DMliKiNrxI9Fs/K48A==";
//        byte[] encryptedData = Base64.getDecoder().decode(data);
        String data ="0c85f400eec54765c4c442fda17f1bbe704184073dbbec23a2b71d64f35ac5b9747939e739290266c85a368f407b0e8821bf2bc5f1a353f47bba4edd10b457eaaf480d4fd9b0583fb227336f3cf0f20f467aa07971031c0403290953c67b53ca124c36db555ce5320bc5314b3fbd9b3cbf7285021c617f0bec989c41a9c3f9d56978f428f55aa28f8f497308e22d78937ffd7521942c1425ddad609c2313d244d695f1966e8080fffb43f34ca2d6cb0ea1a1f40a27a4fc0c90d08c1a3f8d0e1d543daa7e315c047b2f447df886fc29fe1e8679e73a94944b563edffd24ed203929bf1bfdce3ba176cb1843e5e38b70d105f87552691590dc3fc1bab6942763b9";

//        String decryptedText = decrypt(encryptedData, privateKey);
        String decryptedText = decrypt(Hex.decodeHex(data), privateKey);
        System.out.println("解密后的数据：" + decryptedText);

    }


    @Test
    public void test3() throws Exception {
        // 使用私钥解密数据
        String data ="NRQvl3Rti8UPeL7+Ani134wT1qSG5vqdSnZP2ZLeVp8gOmMrFFes6IPZwmALfrB7ZsQUxzD/Rry0YH86ss5Lk1r078X93Og5TJIHtuOWEsDftqOeEW458FpyodC7tQmYk9kQqFUJ5X/GPx4idTtU5i4WxfKvCv7t4imcKtD2RlX5vz1CIa/+XeapkDTiOMUFhNSTuwKlGZtczVMBcHq8gBtZXUzSDAxlvn5PviXeLQy6LTBOwzujCwDxIMH1EJRfI8Cq+b7A3RdBEqfZrXjZgm6AptDIT/Gjljdvmu0lujo4IfZ9O5kBCdqkAX6X0CqJSzA5DMliKiNrxI9Fs/K48A==";
        byte[] encryptedData = Base64.getDecoder().decode(data);
        byte[] decodedData = decryptByPrivateKey(encryptedData, privateKey);
        System.out.println("解密后的数据：" + new String(decodedData, "utf-8"));

    }

    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > 128) {
                cache = cipher.doFinal(encryptedData, offSet, 128);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * 128;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
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
