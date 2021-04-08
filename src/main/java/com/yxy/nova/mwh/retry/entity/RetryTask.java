/**
 * Copyright (c) 2016, Cana and/or its affiliates. All rights reserved.
 */
package com.yxy.nova.mwh.retry.entity;

import java.io.Serializable;
import java.util.Date;

public class RetryTask implements Serializable {

    /**
     * 唯一主键
     */
    private Long   id;

    /**
     *任务类型
     */
    private String taskType;

    /**
     *任务id
     */
    private String taskId;

    /**
     *后退策略
     */
    private String backoffPolicy;

    /**
     *最大重试次数
     */
    private Integer maxAttempt;

    /**
     *当前重试次数
     */
    private Integer currentAttempt;

    /**
     * 最早开始执行时间
     */
    private Date earliestExecutionTime;

    /**
     *任务最晚执行时间
     */
    private Date taskDeadline;

    /**
     *两次重试固定执行间隔，单位秒
     */
    private Integer fixedBackoffPeriod;

    /**
     *状态
	not_begin: 未开始
	processing: 处理中
	success: 处理成功
	fail: 处理失败
     */
    private String state;

    /**
     *执行信息
     */
    private String message;

    /**
     *数据
     */
    private String data;

    /**
     *业务事务版本号， 用于跨request的事务控制
     */
    private String businessTransactionVersion;

    /**
     *处理超时时间， 单位秒， 如果为0代表不超时
     */
    private Integer processTimeout;

    /**
     *该任务是否已经结束
     */
    private Boolean finished;

    /**
     *任务最近一次执行的开始时间
     */
    private Date startTime;

    /**
     *任务最近一次执行的结束时间
     */
    private Date endTime;

    /**
     * 所属分片编号
     */
    private Integer shardingNumber;

    /**
     * 该数据所属机房
     */
    private String idc;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModify;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType == null ? null : taskType.trim();
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    public String getBackoffPolicy() {
        return backoffPolicy;
    }

    public void setBackoffPolicy(String backoffPolicy) {
        this.backoffPolicy = backoffPolicy == null ? null : backoffPolicy.trim();
    }

    public Integer getMaxAttempt() {
        return maxAttempt;
    }

    public void setMaxAttempt(Integer maxAttempt) {
        this.maxAttempt = maxAttempt;
    }

    public Integer getCurrentAttempt() {
        return currentAttempt;
    }

    public void setCurrentAttempt(Integer currentAttempt) {
        this.currentAttempt = currentAttempt;
    }

    public Date getEarliestExecutionTime() {
        return earliestExecutionTime;
    }

    public void setEarliestExecutionTime(Date earliestExecutionTime) {
        this.earliestExecutionTime = earliestExecutionTime;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data == null ? null : data.trim();
    }

    public String getBusinessTransactionVersion() {
        return businessTransactionVersion;
    }

    public void setBusinessTransactionVersion(String businessTransactionVersion) {
        this.businessTransactionVersion = businessTransactionVersion == null ? null : businessTransactionVersion.trim();
    }

    public Integer getProcessTimeout() {
        return processTimeout;
    }

    public void setProcessTimeout(Integer processTimeout) {
        this.processTimeout = processTimeout;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getShardingNumber() {
        return shardingNumber;
    }

    public void setShardingNumber(Integer shardingNumber) {
        this.shardingNumber = shardingNumber;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    public String getIdc() {
        return idc;
    }

    public void setIdc(String idc) {
        this.idc = idc;
    }
}