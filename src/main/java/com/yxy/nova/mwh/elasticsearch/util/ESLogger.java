package com.yxy.nova.mwh.elasticsearch.util;

import ch.qos.logback.classic.Level;
import com.yxy.nova.mwh.elasticsearch.admin.Connection;
import com.yxy.nova.mwh.elasticsearch.admin.ConnectionImpl;
import com.yxy.nova.mwh.elasticsearch.dto.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ESLogger {

    private static Logger logger = LoggerFactory.getLogger(ESLogger.class);

    // 默认warn
    private static Level logLevel = Level.WARN;

    public static void setLoggerLevel(Level level) {
        logLevel = level;
    }

    public static void setLoggerLevel(String level) {
        logLevel = Level.toLevel(level);
    }

    public static String getLoggerLevel() {
        return logLevel.toString();
    }

    public static boolean isDebugEnabled() {
        return Level.INFO.isGreaterOrEqual(logLevel);
    }

    public static boolean isWarnEnabled() {
        return Level.WARN.isGreaterOrEqual(logLevel);
    }

    public boolean isErrorEnabled() {
        return Level.ERROR.isGreaterOrEqual(logLevel);
    }

    public static void curlLog(Connection connection, String method, String uri, Object body) {
        try {
            // info级别大于等于配置级别,才输出
            if (isDebugEnabled()) {
                if (connection instanceof ConnectionImpl) {
                    Config config = ((ConnectionImpl) connection).getConfig();
                    String host = config.getHostList() != null ? config.getHostList().split(",")[0] : " - ";
                    String curlExample = String.format("[%s] curl -u %s:%s -X %s '%s%s' -d '%s'"
                            , config.getClusterName(), config.getUsername(), config.getPassword(), method, host, uri, body);
                    String lastLocation = Thread.currentThread().getStackTrace().length >= 3 ? Thread.currentThread().getStackTrace()[3].toString() : " - ";
                    logger.info("Elasticsearch location:{} curlExample: {} ", lastLocation, curlExample);
                } else {
                    logger.info("Elasticsearch {}", body);
                }
            }
        } catch (Exception e) {
            logger.error("esLog error:", e);
        }
    }

    public static void main(String[] args) {
        System.out.println(logLevel);

        // info级别大于等于配置级别,才输出
        System.out.println(Level.INFO.isGreaterOrEqual(logLevel));
        System.out.println(Level.WARN.isGreaterOrEqual(logLevel));

        setLoggerLevel(Level.INFO);

        System.out.println(logLevel);
        System.out.println(Level.INFO.isGreaterOrEqual(logLevel));
        System.out.println(Level.WARN.isGreaterOrEqual(logLevel));
    }
}
