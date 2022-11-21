package com.yxy.nova.mwh.retry.scheduler;

import com.yxy.nova.mwh.retry.api.IdcResolver;
import com.yxy.nova.mwh.retry.api.RetryTaskHandler;
import com.yxy.nova.mwh.retry.entity.RetryTask;
//import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
//import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class RetryTaskElasticJob implements SimpleJob {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute (ShardingContext shardingContext) {

        logger.info("{}处理重试任务开始,shardingItem:{}", Thread.currentThread().getName() + shardingContext.getJobName(), shardingContext.getShardingItem());

        try {

            String jobName = shardingContext.getJobName();
            int shardingItem = shardingContext.getShardingItem();

            RetryTaskScheduler scheduler = RetryTaskSchedulerRegistry.get(jobName);

            // 解析需要哪些机房的数据。如果idcResolver为null，代表不考虑机房因素。
            List<String> idcList = null;
            IdcResolver idcResolver = scheduler.getIdcResolver();
            if (idcResolver != null) {
                idcList = idcResolver.resolve();
                if (CollectionUtils.isEmpty(idcList)) {
                    logger.info("指定了IdcResolver，但返回的机房列表为空，不处理");
                    return;
                }
            }

            // 拉取准备就绪的任务。idcList为空，代表不考虑机房因素
            List<RetryTask> tasks = scheduler.getConfiguration().getRetryTaskRepository().fetchReadyRetryTasks(shardingItem,
                    scheduler.getHandlers().keySet(), idcList);
            logger.info("发现" + tasks.size() + "个任务");
            for (RetryTask task : tasks) {
                RetryTaskHandler handler = scheduler.getHandlers().get(task.getTaskType());
                scheduler.getExecutor().execute(new RetryTaskTemplate(scheduler, task, handler));
            }

            logger.info("处理重试任务结束");
        } catch (Exception e) {
            logger.error("处理重试任务出现异常", e);
        }

    }
//    @Override
//    public void process(JobExecutionMultipleShardingContext jobExecutionMultipleShardingContext) {
//
//        logger.info("处理重试任务开始");
//
//        try {
//
//            String jobName = jobExecutionMultipleShardingContext.getJobName();
//            List<Integer> shardingItems = jobExecutionMultipleShardingContext.getShardingItems();
//
//            RetryTaskScheduler scheduler = RetryTaskSchedulerRegistry.get(jobName);
//
//            // 解析需要哪些机房的数据。如果idcResolver为null，代表不考虑机房因素。
//            List<String> idcList = null;
//            IdcResolver idcResolver = scheduler.getIdcResolver();
//            if (idcResolver != null) {
//                idcList = idcResolver.resolve();
//                if (CollectionUtils.isEmpty(idcList)) {
//                    logger.info("指定了IdcResolver，但返回的机房列表为空，不处理");
//                    return;
//                }
//            }
//
//            // 拉取准备就绪的任务。idcList为空，代表不考虑机房因素
//            List<RetryTask> tasks = scheduler.getConfiguration().getRetryTaskRepository().fetchReadyRetryTasks(shardingItems,
//                    scheduler.getHandlers().keySet(), idcList);
//            logger.info("发现" + tasks.size() + "个任务");
//            for (RetryTask task : tasks) {
//                RetryTaskHandler handler = scheduler.getHandlers().get(task.getTaskType());
//                scheduler.getExecutor().execute(new RetryTaskTemplate(scheduler, task, handler));
//            }
//
//            logger.info("处理重试任务结束");
//        } catch (Exception e) {
//            logger.error("处理重试任务出现异常", e);
//        }
//
//    }

}
