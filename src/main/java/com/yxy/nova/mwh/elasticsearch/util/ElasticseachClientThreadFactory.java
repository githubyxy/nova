package com.yxy.nova.mwh.elasticsearch.util;

import java.util.concurrent.ThreadFactory;

/**
 * 线程工厂
 * @author quyuanwen
 */
public class ElasticseachClientThreadFactory implements ThreadFactory {

    private String name;

    public ElasticseachClientThreadFactory(String name){
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, this.name);
        thread.setDaemon(Boolean.TRUE);
        return thread;
    }
}