package com.yxy.nova.mwh.retry.handler;

import com.alibaba.fastjson.JSON;
import com.yxy.nova.mwh.retry.api.HandlerStatus;
import com.yxy.nova.mwh.retry.api.RetryTaskExecutionContext;
import com.yxy.nova.mwh.retry.api.RetryTaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yuxiaoyu
 * @Description:
 * @date 2019-07-09 09:20
 */
public class SingleHandler implements RetryTaskHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 执行重试任务, 抛异常或者调用status.fail()都可以标识任务执行失败。
     *
     * @param context 任务数据
     * @param status  处理结果， 如果调用status.fail(), 标识任务处理失败
     * @throws Exception
     */
    @Override
    public void execute(RetryTaskExecutionContext context, HandlerStatus status) throws Exception {
        logger.info("execute 开始执行,线程：{},context：{}", Thread.currentThread().getName(), JSON.toJSONString(context));
    }
}
