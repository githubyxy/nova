package com.yxy.nova.mwh.retry.validator;

import com.yxy.nova.mwh.retry.api.RetryTaskDTO;
import org.apache.commons.lang3.StringUtils;

public class RetryTaskValidator {

    /**
     * 校验用户提交任务的有效性
     * @param retryTaskDTO
     * @throws Exception
     */
    public static void validate(RetryTaskDTO retryTaskDTO, int shardingTotalCount) {
        if (StringUtils.isBlank(retryTaskDTO.getTaskId())) {
            throw new IllegalArgumentException("taskId不能为空");
        }

        if (StringUtils.isBlank(retryTaskDTO.getTaskType())) {
            throw new IllegalArgumentException("taskType不能为空");
        }

        if (retryTaskDTO.getMaxAttempt() == null && retryTaskDTO.getTaskDeadline() == null) {
            throw new IllegalArgumentException("maxAttempt和taskDeadline不能同时为空");
        }

        if (retryTaskDTO.getFixedBackoffPeriod() == null || retryTaskDTO.getFixedBackoffPeriod() <= 0) {
            throw new IllegalArgumentException("fixedBackoffPeriod未配置或者配置不正确");
        }

        if (retryTaskDTO.getProcessTimeout() != null && retryTaskDTO.getProcessTimeout() <= 0) {
            throw new IllegalArgumentException("processTimeout配置不正确");
        }

        if (retryTaskDTO.getShardingNumber() != null && retryTaskDTO.getShardingNumber() < 0) {
            throw new IllegalArgumentException("shardingNumber不能小于0");
        }

        if (retryTaskDTO.getShardingNumber() != null && retryTaskDTO.getShardingNumber() >= shardingTotalCount) {
            throw new IllegalArgumentException("shardingNumber必须小于" + shardingTotalCount);
        }

    }

}
