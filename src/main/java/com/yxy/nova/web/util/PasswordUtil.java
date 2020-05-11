package com.yxy.nova.web.util;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * Created with IntelliJ IDEA.
 *
 * @description: 密码工具类
 * @author: shui.ren
 * @date: 2019-11-21 下午8:20
 */
public class PasswordUtil {

    private static RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    private static String algorithmName = "md5";

    private static int hashIterations = 2;

    public static SaltAndPassword encryptPassword(String plainPassword) {
        // 随机生成盐
        String salt = randomNumberGenerator.nextBytes().toHex();
        // 加密
        String encryptPwd = new SimpleHash(algorithmName, plainPassword,
                ByteSource.Util.bytes(salt), hashIterations).toHex();

        return new SaltAndPassword(salt, encryptPwd);
    }

    public static String encryptPassword(String plainPassword, String salt) {
        // 加密
        String encryptPwd = new SimpleHash(algorithmName, plainPassword,
                ByteSource.Util.bytes(salt), hashIterations).toHex();

        return encryptPwd;
    }

    public static class SaltAndPassword {
        /**
         * 盐
         */
        private String salt;

        /**
         * 加密后的密码
         */
        private String password;

        public SaltAndPassword(String salt, String password) {
            this.salt = salt;
            this.password = password;
        }

        public String getSalt() {
            return salt;
        }

        public String getPassword() {
            return password;
        }
    }

}
