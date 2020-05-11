package com.yxy.nova.elastic.job;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;

/**
 * @author yuxiaoyu
 * @date 2020/5/8 上午10:51
 * @Description
 */
public class TestJob extends AbstractSimpleElasticJob {

    @Override
    public void process(JobExecutionMultipleShardingContext jobExecutionMultipleShardingContext) {
        System.out.println("-----job-------");
        System.out.println(JSON.toJSONString(jobExecutionMultipleShardingContext));
    }
}
