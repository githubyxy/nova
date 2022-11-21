package com.yxy.nova.mwh.utils.log;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 智能交互部日志打印统一工具类
 */
public class LogUtil {

    private static final Logger DEFAULT_MONITOR_LOGGER = LoggerFactory.getLogger("monitor");

    /**
     * 给mdc字段赋值，线程共享
     * @param logConstantField 参考 LogConstants#MDC_FIELD_LIST
     * @param value
     */
    public static final void mdc(String logConstantField, String value) {
        MDC.put(logConstantField, value);
    }

    /**
     * Log a message at the TRACE level according to the specified format and arguments.
     * @param logger
     * @param format
     * @param arguments
     * @param boundary
     */
    public static final void trace(Logger logger, String format, List<Object> arguments, String boundary) {
        if(logger.isTraceEnabled()) {
            LogMeta logMeta = fillRequiredField(format, arguments, boundary);
            logger.trace(logMeta.getFormat(), logMeta.getArguments());
        }
    }

    /**
     * Log a message at the DEBUG level according to the specified format and arguments.
     * @param logger
     * @param format
     * @param arguments
     * @param boundary
     */
    public static final void debug(Logger logger, String format, List<Object> arguments, String boundary) {
        if(logger.isDebugEnabled()) {
            LogMeta logMeta = fillRequiredField(format, arguments, boundary);
            logger.debug(logMeta.getFormat(), logMeta.getArguments());
        }
    };

    /**
     * Log a message at the INFO level according to the specified format and arguments.
     * @param logger
     * @param format
     * @param arguments
     * @param boundary
     */
    public static final void info(Logger logger, String format, List<Object> arguments, String boundary) {
        LogMeta logMeta = fillRequiredField(format, arguments, boundary);
        logger.info(logMeta.getFormat(), logMeta.getArguments());
    }

    /**
     * Log a message at the WARN level according to the specified format and arguments.
     * @param logger
     * @param format
     * @param arguments
     * @param boundary
     */
    public static final void warn(Logger logger, String format, List<Object> arguments, String boundary) {
        LogMeta logMeta = fillRequiredField(format, arguments, boundary);
        logger.warn(logMeta.getFormat(), logMeta.getArguments());
    }

    /**
     * Log a message at the ERROR level according to the specified format and arguments.
     * @param logger
     * @param format
     * @param arguments 如果需要打印异常栈,exception对象放在arguments最后
     * @param boundary
     */
    public static final void error(Logger logger, String format, List<Object> arguments, String boundary) {
        LogMeta logMeta = fillRequiredField(format, arguments, boundary);
        logger.error(logMeta.getFormat(), logMeta.getArguments());
    }

    private static LogMeta fillRequiredField(String format, List<Object> arguments, String boundary) {
        int argSize = 0;
        if(null != arguments) {
            argSize = arguments.size();
        }
        List<Object> newArgList = new ArrayList<>(argSize + LogConstants.LOG_FIELD_LIST.size() + 1);
        StringBuilder builder = new StringBuilder(format.length() + LogConstants.LOG_FIELD_LIST.size() * 5);

        Map<String, Object> map = new HashMap<>(1);
        map.put(LogConstants.BOUNDRAY, boundary);

        for(String field : LogConstants.LOG_FIELD_LIST) {
            Object value = getFieldValue(field, map);
            if(null != value) {
                builder.append(field).append(": {}, ");
                newArgList.add(value);
            }
        }

        builder.append(format);
        if(null != arguments) {
            for(Object arg : arguments) {
                newArgList.add(arg);
            }
        }

        return new LogMeta(builder.toString(), newArgList.toArray());
    }

    private static Object getFieldValue(String field, Map<String, Object> map) {
        if(LogConstants.MDC_FIELD_LIST.contains(field)) {
            return MDC.get(field);
        }
        return map.get(field);
    }

    /**
     * logback.xml中monitor日志配置与wiki(http://wiki.guixi.me/pages/viewpage.action?pageId=31634280)上一致，
     * 即logger name为monitor时可调用此方法
     * @param action 具体操作
     * @param reasonCode 状态码
     * @param spendTime 耗时(ms)
     * @param result 操作结果
     */
    public static final void monitor(String action, String reasonCode, Long spendTime, Object result) {
        monitor(DEFAULT_MONITOR_LOGGER, action, reasonCode, spendTime, result);
    }

    /**
     * 打印监控类日志,会与mokona和dclog打通
     * @param logger
     * @param action 具体操作
     * @param reasonCode 状态码
     * @param spendTime 耗时(ms)
     * @param result 操作结果
     */
    public static final void monitor(Logger logger, String action, String reasonCode, Long spendTime, Object result) {
        Map<String, Object> map = new HashMap<>(4);
        map.put(LogConstants.ACTION, action);
        map.put(LogConstants.REASON_CODE, reasonCode);
        map.put(LogConstants.SPEND_TIME, spendTime);
        if(null != result && NumberUtils.isNumber(result.toString())) {
            map.put(LogConstants.RESULT_NUM, result);
        }else {
            map.put(LogConstants.RESULT_STR, result);
        }
        List<Object> arguments = fillMonitorAgruments(map);
        logger.info(LogConstants.MONITOR_FORMAT, arguments.toArray());
    }

    private static List<Object> fillMonitorAgruments(Map<String, Object> map) {
        List<Object> arguments = new ArrayList<>(LogConstants.MONITOR_FIELD_LIST.size());
        for(String field : LogConstants.MONITOR_FIELD_LIST) {
            Object value = getFieldValue(field, map);
            if(null == value) {
                value = LogConstants.MONITOR_DEFAULT_VALUE;
            }
            arguments.add(value);
        }
        return arguments;
    }

    static class LogMeta {
        String format;
        Object[] arguments;

        public LogMeta(String format, Object[] arguments) {
            this.format = format;
            this.arguments = arguments;
        }

        public String getFormat() {
            return format;
        }

        public Object[] getArguments() {
            return arguments;
        }
    }
}
