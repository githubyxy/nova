package com.yxy.nova.elastic.job;

import com.yxy.nova.mwh.retry.RetryTaskTypeEnum;
import com.yxy.nova.mwh.retry.api.RetryTaskDTO;
import com.yxy.nova.mwh.retry.service.RetryTaskService;
import com.yxy.nova.mwh.utils.UUIDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yuxiaoyu
 * @date 2021/3/29 下午2:21
 * @Description
 */
@Slf4j
public class ElasticTestJob implements SimpleJob {

    @Autowired
    private RetryTaskService retryTaskService;

    @Override
    public void execute(ShardingContext shardingContext) {
//        log.info("ApacheTestJob begin");
        log.info(shardingContext.toString());
//        log.info("ApacheTestJob end");

//        submitNewRetryTask(RetryTaskTypeEnum.MULTITHREAD);
//        submitNewRetryTask(RetryTaskTypeEnum.SINGLETHREAD);
    }

    private void submitNewRetryTask(RetryTaskTypeEnum taskTypeEnum) {
        RetryTaskDTO task = new RetryTaskDTO();
        task.setTaskId(UUIDGenerator.generate());

        task.setData("");
        task.setTaskType(taskTypeEnum.name());
        // 重试间隔10s
        task.setFixedBackoffPeriod(10);
        // 最多回调3次
        task.setMaxAttempt(3);
        retryTaskService.submitNewRetryTask(task);
    }

}
