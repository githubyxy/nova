package com.yxy.nova.mwh.utils.concurrent;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 通用的异步工具类
 */
public class AsyncUtil {
    /**
     * 线程池
     */
    private static ThreadPoolExecutor executor;

    static {
        // 初始化线程池
        int workerThreadPoolSize = Runtime.getRuntime().availableProcessors() * 2;
        executor =new ThreadPoolExecutor(workerThreadPoolSize, workerThreadPoolSize, 5L,TimeUnit.MINUTES,
                new LinkedBlockingQueue<Runnable>(1000),
                new CustomPrefixThreadFactory("AsyncUtil-thread"));
        executor.allowCoreThreadTimeOut(true);
    }

    /**
     * 执行命令
     * @param command
     */
    public static void execute(Runnable command) {
        executor.execute(command);
    }
}
