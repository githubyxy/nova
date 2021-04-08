package com.yxy.nova.mwh.retry.api;

import java.io.Serializable;
import java.util.Date;

public class RetryTaskDTO implements Serializable{

    /**
     *任务类型
     */
    private String taskType;

    /**
     *任务id
     */
    private String taskId;


    /**
     *最大重试次数
     */
    private Integer maxAttempt;

    /**
     * 首次执行需要延后的时间，单位: 秒
     */
    private Integer initialDelay;

    /**
     *任务最晚执行时间
     */
    private Date taskDeadline;

    /**
     *两次重试固定执行间隔，单位秒
     */
    private Integer fixedBackoffPeriod;


    /**
     *数据
     */
    private String data;

    /**
     *处理超时时间， 单位秒， 如果为0代表不超时
     */
    private Integer processTimeout;


    /**
     * 所属分片编号
     */
    private Integer shardingNumber;

    /**
     * 该数据所属机房
     */
    private String idc;

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }


    public Integer getMaxAttempt() {
        return maxAttempt;
    }

    public void setMaxAttempt(Integer maxAttempt) {
        this.maxAttempt = maxAttempt;
    }

    public Date getTaskDeadline() {
        return taskDeadline;
    }

    public void setTaskDeadline(Date taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public Integer getFixedBackoffPeriod() {
        return fixedBackoffPeriod;
    }

    public void setFixedBackoffPeriod(Integer fixedBackoffPeriod) {
        this.fixedBackoffPeriod = fixedBackoffPeriod;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getProcessTimeout() {
        return processTimeout;
    }

    public void setProcessTimeout(Integer processTimeout) {
        this.processTimeout = processTimeout;
    }

    public Integer getShardingNumber() {
        return shardingNumber;
    }

    public void setShardingNumber(Integer shardingNumber) {
        this.shardingNumber = shardingNumber;
    }

    public Integer getInitialDelay() {
        return initialDelay;
    }

    public void setInitialDelay(Integer initialDelay) {
        this.initialDelay = initialDelay;
    }

    public String getIdc() {
        return idc;
    }

    public void setIdc(String idc) {
        this.idc = idc;
    }
}
