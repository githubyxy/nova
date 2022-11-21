package com.yxy.nova.mwh.retry.service;


import com.yxy.nova.mwh.retry.api.RetryTaskDTO;

public interface RetryTaskService {

    /**
     * 提交新的重试任务, 如果任务已存在返回true，否则返回false
     * @param retryTaskDTO
     * @return
     */
    boolean submitNewRetryTask(RetryTaskDTO retryTaskDTO);

    /**
     * 取消任务。如果任务不存在或者任务已经执行完，返回false, 否则返回true。
     * @param taskType
     * @param taskId
     * @return
     */
    boolean cancelRetryTask(String taskType, String taskId);
}
