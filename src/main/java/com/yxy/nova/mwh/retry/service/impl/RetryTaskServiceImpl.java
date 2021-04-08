package com.yxy.nova.mwh.retry.service.impl;

import com.yxy.nova.mwh.retry.api.RetryTaskDTO;
import com.yxy.nova.mwh.retry.config.RetryConfiguration;
import com.yxy.nova.mwh.retry.convertor.RetryTaskConverter;
import com.yxy.nova.mwh.retry.entity.RetryTask;
import com.yxy.nova.mwh.retry.repository.RetryTaskRepository;
import com.yxy.nova.mwh.retry.service.RetryTaskService;
import com.yxy.nova.mwh.retry.validator.RetryTaskValidator;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;

public class RetryTaskServiceImpl implements RetryTaskService {

    private RetryConfiguration configuration;
    private RetryTaskRepository repository;

    @PostConstruct
    public void init() {
        repository = configuration.getRetryTaskRepository();
    }

    @Override
    public boolean submitNewRetryTask(RetryTaskDTO retryTaskDTO) {
        // 校验参数
        RetryTaskValidator.validate(retryTaskDTO, configuration.getShardingTotalCount());
        // 将DTO转换为实体
        final RetryTask taskEntity = RetryTaskConverter.retryTaskDTO2RetryTask(retryTaskDTO, configuration.getShardingTotalCount());

        // 在事务中判断任务是否存在，如果不存在则插入新任务
        TransactionTemplate transactionTemplate = new TransactionTemplate(configuration.getTransactionManager());
        transactionTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRED);

        return transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                if (repository.lockByTaskTypeAndTaskId(taskEntity.getShardingNumber(), taskEntity.getTaskType(), taskEntity.getTaskId()) != null) {
                    return false;
                } else {
                    repository.insertSelective(taskEntity);
                    return true;
                }
            }
        });

    }

    @Override
    public boolean cancelRetryTask(String taskType, String taskId) {
        RetryTask retryTask = repository.getByTaskTypeAndTaskId(null, taskType, taskId);
        if (retryTask == null) {
            return false;
        }
       return repository.cancelRetryTask(retryTask.getShardingNumber(), retryTask.getId());
    }


    public RetryConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(RetryConfiguration configuration) {
        this.configuration = configuration;
    }



}
