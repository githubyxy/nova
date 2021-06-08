package com.yxy.nova.elastic.job;

import org.apache.shardingsphere.elasticjob.infra.handler.sharding.JobInstance;
import org.apache.shardingsphere.elasticjob.infra.handler.sharding.JobShardingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yuxiaoyu
 * @date 2021/6/8 下午2:44
 * @Description
 */
public class MyJobShardingStrategy implements JobShardingStrategy {

    public MyJobShardingStrategy() {

    }

    /**
     * Sharding job.
     *
     * @param jobInstances       all job instances which participate in sharding
     * @param jobName            job name
     * @param shardingTotalCount sharding total count
     * @return sharding result
     */
    @Override
    public Map<JobInstance, List<Integer>> sharding(List<JobInstance> jobInstances, String jobName, int shardingTotalCount) {

        Map<JobInstance, List<Integer>> result = null;
        List<Integer> shardingItems = new ArrayList<>(shardingTotalCount + 1);
        for (int i=0; i<shardingTotalCount; i++) {
            shardingItems.add(i);
        }
        result.put(jobInstances.get(0), shardingItems);
        return result;
    }

    /**
     * Get type.
     *
     * @return type
     */
    @Override
    public String getType() {
        return "MY_TEST";
    }
}
