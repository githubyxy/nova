package com.yxy.nova.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yuxiaoyu
 * @date 2019-05-06 15:14
 * @Description 线程池拒绝策略--等待100ms，然后尝试执行
 */
public class CustomRejectedPolicy implements RejectedExecutionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        if (!executor.isShutdown()) {
            executor.execute(r);
        }
    }
}
