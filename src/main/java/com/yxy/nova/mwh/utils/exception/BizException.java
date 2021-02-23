package com.yxy.nova.mwh.utils.exception;

import com.yxy.nova.mwh.client.reasoncode.ReasonCode;

/**
 * @author: shui.ren
 */
public class BizException extends RuntimeException {
    private static final long serialVersionUID = 430305575267731710L;
    private String reasonDesc = "";
    private ReasonCode reasonCode;

    public static BizException instance(ReasonCode reasonCode) {
    	return new BizException(reasonCode, reasonCode.getDesc());
    }

	public static BizException instance(String reasonDesc) {
		return new BizException(ReasonCode.SYS_ERROR, reasonDesc);
	}

    public static BizException instance(String reasonDesc, Throwable thr) {
        return new BizException(ReasonCode.SYS_ERROR, reasonDesc, thr);
    }

    public static BizException instance(ReasonCode reasonCode, String reasonDesc) {
        return new BizException(reasonCode, reasonDesc);
    }

    public static BizException instance(ReasonCode reasonCode, String reasonDesc, Throwable thr) {
        return new BizException(reasonCode, reasonDesc, thr);
    }

    public static BizException instance(ReasonCode reasonCode, Throwable thr) {
        return new BizException(reasonCode, reasonCode.getDesc(), thr);
    }

    private BizException(ReasonCode reasonCode, String reasonDesc, Throwable thr) {
        super(String.format("[reasonCode=%s,reasonDesc=%s]", reasonCode.getCode(), reasonDesc));
        this.reasonCode = reasonCode;
        this.reasonDesc = reasonDesc;
        this.initCause(thr);
    }


    public BizException(ReasonCode reasonCode, String reasonDesc) {
        super(String.format("[reasonCode=%s,reasonDesc=%s]", reasonCode.getCode(), reasonDesc));
        this.reasonCode = reasonCode;
        this.reasonDesc = reasonDesc;
    }

    public String getReasonDesc() {
        return reasonDesc;
    }

    public ReasonCode getReasonCode() {
        return reasonCode;
    }

    @Override
    public String toString() {
        return getMessage();
    }

    @Override
    public String getMessage() {
        return String.format("[reasonCode=%s,reasonDesc=%s]", this.reasonCode, this.reasonDesc);
    }

    /**
     * 从thr中提取BizException,若提取不到返回thr
     * @param e
     * @return
     */
    public static Exception unwrap(Exception e){
    	Throwable cause = e.getCause();
		if (cause != null && cause instanceof BizException) {
            return (Exception) cause;
        }
		return e;
    }

    /**
     * 获取原始的消息
     * @param e
     * @return
     */
    public static String getRawMsg(Exception e){
    	if (e == null) {
            return null;
        }
    	e = unwrap(e);
    	if (e instanceof BizException) {
    		BizException bizException = (BizException)e;
    		return bizException.getReasonDesc();
    	} else {
    	    return e.getMessage();
        }
    }
}
