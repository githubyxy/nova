package com.yxy.nova.mwh.utils.log;

import org.apache.commons.lang3.RandomStringUtils;

public class TraceIdUtil {

    public static final String TRACEID_NAME = "traceId";

    /**
     * 生成新的traceId
     * @return
     */
    public static String genId() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

}
