package com.yxy.nova.cmpp.pojo;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by lqq on 2017/8/9.
 */
public class SmsSendInfo implements Serializable {

    private static final long serialVersionUID = -1068149475448227573L;

    private String            msgId;
    private String            mobile;
    private String            content;
    private Boolean           sendFlag;
    private String            smsOperator;
    // 短信拆分条数
    private Integer           sendAmount;
    private Timestamp         sendTime;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getSendFlag() {
        return sendFlag;
    }

    public void setSendFlag(Boolean sendFlag) {
        this.sendFlag = sendFlag;
    }

    public String getSmsOperator() {
        return smsOperator;
    }

    public void setSmsOperator(String smsOperator) {
        this.smsOperator = smsOperator;
    }

    public Integer getSendAmount() {
        return sendAmount;
    }

    public void setSendAmount(Integer sendAmount) {
        this.sendAmount = sendAmount;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
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
