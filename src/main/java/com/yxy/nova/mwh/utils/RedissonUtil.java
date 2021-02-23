package com.yxy.nova.mwh.utils;

import lombok.SneakyThrows;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: renshui
 * @date: 2020-05-29 10:31 上午
 */
public class RedissonUtil {

    @SneakyThrows
    public static RedissonClient createSingleServerRedissonClient(String redisUri) {
        URI uri = new URI(redisUri);
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + uri.getHost() + ":" + uri.getPort())
                .setPassword(getPassword(uri));
        return Redisson.create(config);
    }

    /**
     * 从url中获取密码
     * @param uri
     * @return
     */
    private static String getPassword(URI uri) {
        String userInfo = uri.getUserInfo();
        if (userInfo == null) {
            return null;
        } else {
            String[] parts = userInfo.split(":", 2);
            if (parts.length > 1) {
                return parts[1];
            } else {
                return null;
            }
        }
    }
}
