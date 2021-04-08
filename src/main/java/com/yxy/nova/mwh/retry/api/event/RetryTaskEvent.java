package com.yxy.nova.mwh.retry.api.event;


import com.yxy.nova.mwh.retry.api.RetryTaskExecutionContext;

import java.io.Serializable;

public class RetryTaskEvent implements Serializable{
    /**
     * 事件类型
     */
    private EventTypeEnum eventType;

    /**
     * 任务执行上下文
     */
    private RetryTaskExecutionContext context;

    /**
     * 执行过程中抛出的异常
     */
    private Throwable throwable;

    /**
     * 执行结果描述
     */
    private String message;

    public EventTypeEnum getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeEnum eventType) {
        this.eventType = eventType;
    }

    public RetryTaskExecutionContext getContext() {
        return context;
    }

    public void setContext(RetryTaskExecutionContext context) {
        this.context = context;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
