package com.yxy.nova.mwh.retry.repository;

import com.yxy.nova.mwh.retry.api.IdcResolver;
import com.yxy.nova.mwh.retry.convertor.RetryTaskConverter;
import com.yxy.nova.mwh.retry.entity.RetryTask;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.*;

/**
 * retry_task表分片
 * @author shui.ren
 */
public class ShardingRetryTaskRepository extends AbstractRetryTaskRepository {

    public ShardingRetryTaskRepository(String databaseTablePrefix, Integer shardingTotalCount, SqlSessionFactory sqlSessionFactory)  {
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
        if (shardingNumber != null) {
            return getByShardingNumberTaskTypeAndTaskId(shardingNumber, taskType, taskId);
        } else {
            // 因为允许客户指定shardingNumber, 所以可能存在实际的shardingNumber和跟taskId计算出来的不一样
            RetryTask retryTask = getByShardingNumberTaskTypeAndTaskId(RetryTaskConverter.getShardingNumber(taskId, shardingTotalCount), taskType, taskId);
            if (retryTask != null) {
                return retryTask;
            }

            for (int i = 0; i < shardingTotalCount; i++) {
                retryTask = getByShardingNumberTaskTypeAndTaskId(i, taskType, taskId);
                if (retryTask != null) {
                    return retryTask;
                }
            }

            return null;
        }
    }

    private RetryTask getByShardingNumberTaskTypeAndTaskId(Integer shardingNumber, String taskType, String taskId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("prefix", databaseTablePrefix);
        parameters.put("shardingNumber", shardingNumber);
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
        parameters.put("shardingNumber", shardingNumber);
        parameters.put("taskType", taskType);
        parameters.put("taskId", taskId);
        return openSession().selectOne(STATEMENT_PREFIX + ".lockByTaskTypeAndTaskId", parameters);
    }

    /**
     * 获取准备就绪的任务
     * @param shardingItem
     * @param taskTypes
     * @return
     */
    @Override
    public List<RetryTask> fetchReadyRetryTasks(Integer shardingItem, Set<String> taskTypes, List<String> idcList) {
        List<RetryTask> resultList = new ArrayList<>(16);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("prefix", databaseTablePrefix);
        parameters.put("taskTypes", taskTypes);
        parameters.put("shardingNumber", shardingItem);
        parameters.put("top", 50);
        if (CollectionUtils.isNotEmpty(idcList)) {
            parameters.put("idcList", idcList);
            if (idcList.contains(IdcResolver.IDC_MISSING)) {
                parameters.put("includeIdcMissing", true);
            }
        }
        resultList.addAll(openSession().selectList(STATEMENT_PREFIX + ".fetchReadyRetryTasks", parameters));

        return resultList;
    }


    /**
     * 根据指定的retentionPolicy删除已经不会再执行的历史任务
     * @param taskType
     * @param retentionEarliestTime
     * @return
     */
    @Override
    public int deleteHistoryTasks(String taskType, Date retentionEarliestTime) {
        int affectedNum = 0;
        for (Integer shardingNumber = 0;  shardingNumber < shardingTotalCount; shardingNumber++) {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("prefix", databaseTablePrefix);
            parameters.put("shardingNumber", shardingNumber);
            parameters.put("taskType", taskType);
            parameters.put("retentionEarliestTime", retentionEarliestTime);
            affectedNum += openSession().delete(STATEMENT_PREFIX + ".deleteHistoryTasks", parameters);
        }
        return affectedNum;
    }

}
