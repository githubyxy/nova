package com.yxy.nova.elastic.job;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yuxiaoyu
 * @date 2020/5/8 上午10:51
 * @Description
 */
@Slf4j
public class TestJob extends AbstractSimpleElasticJob {

    @Override
    public void process(JobExecutionMultipleShardingContext jobExecutionMultipleShardingContext) {
        System.out.println("-----job-------");
        long c = System.currentTimeMillis();
        log.info("处理业务中……");
        while (System.currentTimeMillis() < c + (60 * 1000)) {

        }
        log.info("处理业务结束……");
        System.out.println(JSON.toJSONString(jobExecutionMultipleShardingContext));
    }
}
