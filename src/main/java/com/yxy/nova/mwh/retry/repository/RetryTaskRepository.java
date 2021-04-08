package com.yxy.nova.mwh.retry.repository;

import com.yxy.nova.mwh.retry.entity.RetryTask;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 仓库接口
 * @author shui.ren
 */
public interface RetryTaskRepository {


    /**
     * 根据taskType和taskId获取重试记录
     * @param shardingNumber
     * @param taskType
     * @param taskId
     * @return
     */
    RetryTask getByTaskTypeAndTaskId(Integer shardingNumber, String taskType, String taskId);

    /**
     * 根据taskType和taskId加锁
     * @param shardingNumber
     * @param taskType
     * @param taskId
     * @return
     */
    RetryTask lockByTaskTypeAndTaskId(Integer shardingNumber, String taskType, String taskId);

    /**
     * 插入重试任务
     * @param taskEntity
     * @return
     */
    Integer insertSelective(RetryTask taskEntity);

    /**
     * 获取准备就绪的任务。为了避免就绪的任务太多导致内存溢出，建议实现时加limit。
     * @param shardingItems
     * @param taskTypes
     * @param idcList。 如果为null代表查询时不考虑机房因素。
     * @return
     */
    List<RetryTask> fetchReadyRetryTasks(List<Integer> shardingItems, Set<String> taskTypes, List<String> idcList);

    /**
     * 以乐观锁的方式设置任务开始, 如果更新失败返回false
     * @param shardingNumber
     * @param id
     * @param oldVersion
     * @param newState
     * @param startTime
     * @param newVersion
     * @return 如果执行成功，返回true
     */
    boolean setTaskStart(Integer shardingNumber, Long id, String oldVersion, String newState, Date startTime, String newVersion);

    /**
     * 由于异常终止任务执行
     * @param shardingNumber
     * @param id
     * @param newState
     * @param message
     * @return  如果执行成功，返回true
     */
    boolean terminateTaskAbruptly(Integer shardingNumber, Long id, String newState, String message);

    /**
     * 更新状态
     * @param shardingNumber
     * @param id
     * @param state
     * @return 如果执行成功， 返回true
     */
    boolean updateState(Integer shardingNumber, Long id, String state);

    /**
     * 任务执行完成后，更新记录
     * @param shardingNumber
     * @param id
     * @param newState
     * @param finished
     * @param endTime
     * @param message
     * @return 如果执行成功， 返回true
     */
    boolean endTaskExcution(Integer shardingNumber, Long id, String newState, boolean finished, Date endTime, String message, Integer currentAttempt);

    /**
     * 根据指定的retentionPolicy删除已经不会再执行的历史任务
     * @param taskType
     * @param retentionEarliestTime
     * @return 返回影响的条数
     */
    int deleteHistoryTasks(String taskType, Date retentionEarliestTime);

    /**
     * 取消任务
     * @param shardingNumber
     * @param id
     * @return 如果执行成功， 返回true
     */
    boolean cancelRetryTask(Integer shardingNumber, Long id);
}
