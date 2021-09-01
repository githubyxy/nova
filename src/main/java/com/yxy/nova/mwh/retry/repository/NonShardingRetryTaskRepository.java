package com.yxy.nova.mwh.retry.repository;

import com.yxy.nova.mwh.retry.api.IdcResolver;
import com.yxy.nova.mwh.retry.entity.RetryTask;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.*;

/**
 * retry_task表不分片
 * @author shui.ren
 */
public class NonShardingRetryTaskRepository extends AbstractRetryTaskRepository {

    public NonShardingRetryTaskRepository(String databaseTablePrefix, Integer shardingTotalCount, SqlSessionFactory sqlSessionFactory)  {
        super(databaseTablePrefix, shardingTotalCount, sqlSessionFactory);
    }

    /**
     * 根据taskType和taskId获取重试记录
     * @param shardingNumber
     * @param taskType
     * @param taskId
     * @return
     */
    @Override
    public RetryTask getByTaskTypeAndTaskId(Integer shardingNumber, String taskType, String taskId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("prefix", databaseTablePrefix);
        parameters.put("taskType", taskType);
        parameters.put("taskId", taskId);
        return openSession().selectOne(STATEMENT_PREFIX + ".getByTaskTypeAndTaskId", parameters);
    }

    /**
     * 根据taskType和taskId加锁
     * @param shardingNumber
     * @param taskType
     * @param taskId
     * @return
     */
    @Override
    public RetryTask lockByTaskTypeAndTaskId(Integer shardingNumber, String taskType, String taskId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("prefix", databaseTablePrefix);
        parameters.put("taskType", taskType);
        parameters.put("taskId", taskId);
        return openSession().selectOne(STATEMENT_PREFIX + ".lockByTaskTypeAndTaskId", parameters);
    }

    /**
     * 获取准备就绪的任务
     * @param shardingItems
     * @param taskTypes
     * @return
     */
    @Override
    public List<RetryTask> fetchReadyRetryTasks(List<Integer> shardingItems, Set<String> taskTypes, List<String> idcList) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("prefix", databaseTablePrefix);
        parameters.put("taskTypes", taskTypes);
//        parameters.put("shardingItems", shardingItems);
        parameters.put("top", 200);
        if (CollectionUtils.isNotEmpty(idcList)) {
            parameters.put("idcList", idcList);
            if (idcList.contains(IdcResolver.IDC_MISSING)) {
                parameters.put("includeIdcMissing", true);
            }
        }

        return openSession().selectList(STATEMENT_PREFIX + ".fetchReadyRetryTasks", parameters);
    }

    /**
     * 根据指定的retentionPolicy删除已经不会再执行的历史任务
     * @param taskType
     * @param retentionEarliestTime
     * @return
     */
    @Override
    public int deleteHistoryTasks(String taskType, Date retentionEarliestTime) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("prefix", databaseTablePrefix);
        parameters.put("taskType", taskType);
        parameters.put("retentionEarliestTime", retentionEarliestTime);

        return openSession().delete(STATEMENT_PREFIX +".deleteHistoryTasks", parameters);
    }
}
