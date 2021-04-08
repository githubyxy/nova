package com.yxy.nova.mwh.retry.scheduler;

import java.util.HashMap;
import java.util.Map;

/**
 * elasticJob通过此注册中心取到运行它的scheduler, 然后通过此scheduler拿到运行过程中需要的对象引用
 */
public class RetryTaskSchedulerRegistry {

    private static Map<String, RetryTaskScheduler> registry = new HashMap<>();

    public static void register(String jobName, RetryTaskScheduler scheduler) {
        registry.put(jobName, scheduler);
    }

    public static RetryTaskScheduler get(String jobName) {
        return registry.get(jobName);
    }

}
