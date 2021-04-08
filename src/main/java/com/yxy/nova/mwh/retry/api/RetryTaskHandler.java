package com.yxy.nova.mwh.retry.api;

public interface RetryTaskHandler {

    /**
     * 执行重试任务, 抛异常或者调用status.fail()都可以标识任务执行失败。
     * @param context 任务数据
     * @param status 处理结果， 如果调用status.fail(), 标识任务处理失败
     * @throws Exception
     */
    public void execute(RetryTaskExecutionContext context, final HandlerStatus status) throws Exception;
}
