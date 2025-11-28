package com.yxy.nova.mwh.utils.concurrent;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.SneakyThrows;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author yuxiaoyu
 * @date 2021/5/29 下午5:53
 * @Description
 */
public class InJvmLockUtil {

    private static LoadingCache<String, Semaphore> semaphores = CacheBuilder.newBuilder()
            // 设置并发级别为cpu核心数
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build(new CacheLoader<String, Semaphore>() {
                @Override
                public Semaphore load(String lockKey) throws Exception {
                    return new Semaphore(1);
                }
            });

    /**
     * 运行在锁中
     * @param lockKey
     * @param runnable
     */
    @SneakyThrows
    public static void runInLock(String lockKey,
                                 Runnable runnable) {
        // 获取互斥锁
        Semaphore mutex = semaphores.getUnchecked(lockKey);

//        // 加锁
        if (mutex.tryAcquire(1, TimeUnit.SECONDS)) {
            try {
                runnable.run();
            } catch (Exception e) {
                throw e;
            } finally {
                mutex.release();
            }
        }

//         加锁
//            try {
//                mutex.acquire();
//                runnable.run();
//            } catch (Exception e) {
//                throw e;
//            } finally {
//                mutex.release();
//            }

    }

}
