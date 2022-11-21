package com.yxy.nova.mwh.eunomia.client.exception;

/**
 * Created by toruneko on 2016/12/7.
 */
public class EunomiaClientException extends RuntimeException {

    public EunomiaClientException() {
        super();
    }

    public EunomiaClientException(String message) {
        super(message);
    }

    public EunomiaClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public EunomiaClientException(Throwable cause) {
        super(cause);
    }

    protected EunomiaClientException(String message, Throwable cause, boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
