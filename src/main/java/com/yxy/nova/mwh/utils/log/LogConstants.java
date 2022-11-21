package com.yxy.nova.mwh.utils.log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 定义日志常量
 */
public class LogConstants {

    /**
     * 字段常量池
     */
    public static final String BIZ_ID = "bizId";
    public static final String TRACE_ID = "traceId";
    public static final String PRODUCT = "product";
    public static final String PARTNER = "partner";
    public static final String APP = "app";
    // 更细粒度
    public static final String GRAIN = "grain";
    // 职责边界
    public static final String BOUNDRAY = "boundary";
    // 具体操作
    public static final String ACTION = "action";
    // 状态码
    public static final String REASON_CODE = "reasonCode";
    // 耗时(ms)
    public static final String SPEND_TIME = "spendTime";
    // 操作结果-非数值类，tag
    public static final String RESULT_STR = "result_str";
    // 操作结果-数值类，field
    public static final String RESULT_NUM = "result_num";


    static final String MONITOR_DEFAULT_VALUE = "-";

    /**
     * 通用日志必须字段
     */
    static final List<String> LOG_FIELD_LIST = Arrays.asList(LogConstants.BIZ_ID, LogConstants.TRACE_ID, LogConstants.PRODUCT, LogConstants.PARTNER, LogConstants.APP, LogConstants.BOUNDRAY);

    /**
     * 监控日志必须字段
     */
    static final List<String> MONITOR_FIELD_LIST = Arrays.asList(LogConstants.BIZ_ID, LogConstants.PRODUCT, LogConstants.PARTNER, LogConstants.APP, LogConstants.GRAIN, LogConstants.ACTION, LogConstants.REASON_CODE, LogConstants.SPEND_TIME, LogConstants.RESULT_STR, LogConstants.RESULT_NUM);
    static final String MONITOR_FORMAT;
    static {
        StringBuilder builder = new StringBuilder();
        for (int i=0;i < MONITOR_FIELD_LIST.size();i++) {
            builder.append("{} ");
        }
        MONITOR_FORMAT = builder.toString();
    }

    static final Set<String> MDC_FIELD_LIST = new HashSet<>(Arrays.asList(LogConstants.BIZ_ID, LogConstants.TRACE_ID, LogConstants.PRODUCT, LogConstants.PARTNER, LogConstants.APP, LogConstants.GRAIN));
}
