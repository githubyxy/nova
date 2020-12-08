package com.yxy.nova.cmpp.pojo;

import com.alibaba.fastjson.JSONObject;

import java.sql.Timestamp;

/**
 * Created by lqq on 2017/7/17.
 */
public class SmsReportResult {

    private String     msgId;
    private String     mobile;
    private Boolean    reportFlag;
    private JSONObject detailResult;
    private Timestamp  reportTime;
    private Long       channelId;
    private String     errorCode;
    private String     errorDesc;

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

    public JSONObject getDetailResult() {
        return detailResult;
    }

    public void setDetailResult(JSONObject detailResult) {
        this.detailResult = detailResult;
    }

    public Timestamp getReportTime() {
        return reportTime;
    }

    public void setReportTime(Timestamp reportTime) {
        this.reportTime = reportTime;
    }

    public String toJSONString() {
        return JSONObject.toJSONString(this);
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
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

    public SmsReportInfo convert() {
        SmsReportInfo reportInfo = new SmsReportInfo();
        reportInfo.setMsgId(this.getMsgId());
        reportInfo.setMobile(this.getMobile());
        reportInfo.setReportFlag(this.getReportFlag());
        reportInfo.setReportTime(this.getReportTime());
        reportInfo.setErrorCode(this.getErrorCode());
        reportInfo.setErrorDesc(this.getErrorDesc());
        return reportInfo;
    }
}
