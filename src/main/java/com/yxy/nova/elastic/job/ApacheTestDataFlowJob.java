package com.yxy.nova.elastic.job;

import com.alibaba.fastjson.JSON;
import com.yxy.nova.mwh.retry.RetryTaskTypeEnum;
import com.yxy.nova.mwh.retry.api.RetryTaskDTO;
import com.yxy.nova.mwh.retry.service.RetryTaskService;
import com.yxy.nova.mwh.utils.UUIDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.dataflow.job.DataflowJob;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuxiaoyu
 * @date 2021/3/29 下午2:21
 * @Description
 */
@Slf4j
public class ApacheTestDataFlowJob implements DataflowJob<Integer> {


    @Override
    public List<Integer> fetchData(ShardingContext shardingContext) {
        List<Integer> list = new ArrayList<>();
        list.add(shardingContext.getShardingItem());
        return list;
    }

    @Override
    public void processData(ShardingContext shardingContext, List<Integer> list) {
        log.info(shardingContext.toString());
        log.info("list:{}", JSON.toJSONString(list));
    }
}
