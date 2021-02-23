package com.yxy.nova.mwh.elasticsearch.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池拒绝策略
 * @author quyuanwen
 */
public class ElasticsearchClientRejectedHandler implements RejectedExecutionHandler {
    private static Logger logger = LoggerFactory.getLogger(ElasticsearchClientRejectedHandler.class);
    private String name;

    public ElasticsearchClientRejectedHandler(String name){
        this.name = name;
    }
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        logger.warn("执行{}操作，线程池已满，线程池最大容量{}，实际活动线程{}, 改线程的处理内容是{}!" ,name, executor.getMaximumPoolSize(), executor.getActiveCount(), r.toString());
       ;
    }
}
