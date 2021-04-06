package com.yxy.nova.elastic.job;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;

/**
 * @author yuxiaoyu
 * @date 2021/3/29 下午2:21
 * @Description
 */
@Slf4j
public class ApacheTestJob implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("ApacheTestJob begin");
        log.info(shardingContext.toString());
        log.info("ApacheTestJob end");
    }

}
