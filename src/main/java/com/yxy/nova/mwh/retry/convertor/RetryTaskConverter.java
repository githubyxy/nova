package com.yxy.nova.mwh.retry.convertor;

import com.yxy.nova.mwh.retry.api.RetryTaskDTO;
import com.yxy.nova.mwh.retry.api.RetryTaskExecutionContext;
import com.yxy.nova.mwh.retry.entity.RetryTask;
import com.yxy.nova.mwh.retry.enums.RetryTaskBackOffPolicyEnum;
import com.yxy.nova.mwh.retry.enums.RetryTaskStateEnum;
import com.yxy.nova.mwh.utils.time.DateTimeUtil;
import org.springframework.beans.BeanUtils;

import java.util.Date;

public class RetryTaskConverter {

    /**
     * 将DTO转换成实体
     * @param retryTaskDTO
     * @param shardingTotalCount
     * @return
     */
    public static RetryTask retryTaskDTO2RetryTask(RetryTaskDTO retryTaskDTO, int shardingTotalCount) {
        RetryTask entity = new RetryTask();
        entity.setTaskType(retryTaskDTO.getTaskType());
        entity.setTaskId(retryTaskDTO.getTaskId());
        entity.setBackoffPolicy(RetryTaskBackOffPolicyEnum.FIXED.name());
        entity.setMaxAttempt(retryTaskDTO.getMaxAttempt());
        if (retryTaskDTO.getInitialDelay() != null) {
            entity.setEarliestExecutionTime(DateTimeUtil.plusSeconds(new Date(), retryTaskDTO.getInitialDelay()));
        }
        entity.setTaskDeadline(retryTaskDTO.getTaskDeadline());
        entity.setFixedBackoffPeriod(retryTaskDTO.getFixedBackoffPeriod());
        entity.setState(RetryTaskStateEnum.NOT_BEGIN.name());
        entity.setData(retryTaskDTO.getData());
        Integer shardingNumber;
        if (retryTaskDTO.getShardingNumber() != null) {
            shardingNumber = retryTaskDTO.getShardingNumber();
        } else {
            shardingNumber = getShardingNumber(retryTaskDTO.getTaskId(), shardingTotalCount);
        }
        entity.setShardingNumber(shardingNumber);
        entity.setIdc(retryTaskDTO.getIdc());
        return entity;
    }

    /**
     * 将实体转换成context
     * @param task
     * @return
     */
    public static RetryTaskExecutionContext retryTask2RetryTaskExecutionContext(RetryTask task) {
        RetryTaskExecutionContext context = new RetryTaskExecutionContext();
        BeanUtils.copyProperties(task, context);
        return context;
    }

    /**
     * 根据taskId计算所属分片
     * @param taskId
     * @param shardingTotalCount
     * @return
     */
    public static int getShardingNumber(String taskId, int shardingTotalCount) {
        return (taskId.hashCode() & 0x7FFFFFFF) % shardingTotalCount;
    }

}
