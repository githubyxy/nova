package com.yxy.nova.mwh.utils.generator;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

/**
 * ID生成器
 * Created by chenchanglong on 2019/9/4.
 */
public class IDGenerator {

    /**
     * 生成10位的随机traceId
     * @return
     */
    public static final String generateTraceId() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    /**
     * 生成不带-的UUID
     * @return
     */
    public static final String generateUuidWithoutUnderLine() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        return uuid;
    }
}
