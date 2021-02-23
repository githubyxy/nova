package com.yxy.nova.mwh.utils.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: renshui
 * @date: 2020-05-30 4:28 下午
 */
public class RedissonLockUtil {

    /**
     * 运营在公平锁中
     * @param redissonClient
     * @param lockKey
     * @param timeout
     * @param timeUnit
     * @param runnable
     */
    public static void runInFairLock(RedissonClient redissonClient,
                                     String lockKey,
                                     long timeout,
                                     TimeUnit timeUnit,
                                     Runnable runnable) {
        // 获取分布式锁
        RLock lock = redissonClient.getFairLock(lockKey);
        try {
            // 加锁
            lock.lock(timeout, timeUnit);
            runnable.run();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 运营在公平锁中。默认超时时间10分钟
     * @param redissonClient
     * @param lockKey
     * @param runnable
     */
    public static void runInFairLock(RedissonClient redissonClient,
                                     String lockKey,
                                     Runnable runnable) {
        runInFairLock(redissonClient, lockKey, 10, TimeUnit.MINUTES, runnable);
    }
}
