package com.yxy.nova.cmpp.pojo;

import com.alibaba.fastjson.JSONObject;

import java.sql.Timestamp;

/**
 * Created by lqq on 2017/7/17.
 */
public class SmsSendResult {

    private String     msgId;
    private String     mobile;
    private String     content;
    private Boolean    sendFlag;
    private String     smsOperator;
    private String     sendMark;      // 标识符(扩展码、发送号码等)
    private Integer    sendAmount = 0;
    private JSONObject detailResult;
    private Timestamp  sendTime;
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

    public JSONObject getDetailResult() {
        return detailResult;
    }

    public void setDetailResult(JSONObject detailResult) {
        this.detailResult = detailResult;
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

    public String getSendMark() {
        return sendMark;
    }

    public void setSendMark(String sendMark) {
        this.sendMark = sendMark;
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

    public SmsSendInfo convert() {
        SmsSendInfo sendInfo = new SmsSendInfo();
        sendInfo.setMsgId(this.getMsgId());
        sendInfo.setMobile(this.getMobile());
        sendInfo.setContent(this.getContent());
        sendInfo.setSendAmount(this.getSendAmount());
        sendInfo.setSendFlag(this.getSendFlag());
        sendInfo.setSmsOperator(this.getSmsOperator());
        sendInfo.setSendTime(this.getSendTime());
        sendInfo.setErrorCode(this.getErrorCode());
        sendInfo.setErrorDesc(this.getErrorDesc());
        return sendInfo;
    }
}
