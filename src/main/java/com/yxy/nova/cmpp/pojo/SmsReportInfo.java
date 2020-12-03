package com.yxy.nova.cmpp.pojo;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by lqq on 2017/8/9.
 */
public class SmsReportInfo implements Serializable {

    private static final long serialVersionUID = -8979051212998279715L;

    private String            msgId;
    private String            mobile;
    private Boolean           reportFlag;
    private Timestamp         reportTime;
    private JSONObject ext;
    private String            errorCode;
    private String            errorDesc;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean getReportFlag() {
        return reportFlag;
    }

    public void setReportFlag(Boolean reportFlag) {
        this.reportFlag = reportFlag;
    }

    public Timestamp getReportTime() {
        return reportTime;
    }

    public void setReportTime(Timestamp reportTime) {
        this.reportTime = reportTime;
    }

    public JSONObject getExt() {
        return ext;
    }

    public void setExt(JSONObject ext) {
        this.ext = ext;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }
}
