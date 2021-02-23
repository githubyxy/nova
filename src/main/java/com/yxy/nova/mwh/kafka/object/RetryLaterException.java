package com.yxy.nova.mwh.kafka.object;

public class RetryLaterException extends RuntimeException {
    //如果IConsumer抛出该异常，则说明当前业务无法正常消费，需要等待并重试
}
