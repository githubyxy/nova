package com.yxy.nova.mwh.utils.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.net.SocketTimeoutException;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: renshui
 * @date: 2020-05-30 10:25 下午
 */
public class ExceptionUtil {

    /**
     * 传入的异常是否由于SocketTimeout导致
     * @param thr
     * @return
     */
    public static boolean isCausedBySocketTimeoutException(Throwable thr) {
        return ExceptionUtils.indexOfType(thr, SocketTimeoutException.class) != -1;
    }
}
