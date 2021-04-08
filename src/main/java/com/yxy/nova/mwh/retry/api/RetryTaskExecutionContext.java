package com.yxy.nova.mwh.retry.api;

import java.io.Serializable;
import java.util.Date;

public class RetryTaskExecutionContext implements Serializable{
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
     *数据
     */
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
