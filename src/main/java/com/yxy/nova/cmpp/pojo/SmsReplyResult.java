package com.yxy.nova.cmpp.pojo;

import com.alibaba.fastjson.JSONObject;

import java.sql.Timestamp;

/**
 * Created by lqq on 2017/7/17.
 */
public class SmsReplyResult {

    private String     moId;
    private String     msgId;
    private String     mobile;
    private String     content;
    private String     sendMark;
    private JSONObject detailResult;
    private Timestamp  replyTime;
    private Long       channelId;

    public String getMoId() {
        return moId;
    }

    public void setMoId(String moId) {
        this.moId = moId;
    }

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

    public String getSendMark() {
        return sendMark;
    }

    public void setSendMark(String sendMark) {
        this.sendMark = sendMark;
    }

    public JSONObject getDetailResult() {
        return detailResult;
    }

    public void setDetailResult(JSONObject detailResult) {
        this.detailResult = detailResult;
    }

    public Timestamp getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Timestamp replyTime) {
        this.replyTime = replyTime;
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


    public SmsReplyInfo convert() {
        SmsReplyInfo replyInfo = new SmsReplyInfo();
        replyInfo.setMsgId(this.getMsgId());
        replyInfo.setContent(this.getContent());
        replyInfo.setMobile(this.getMobile());
        replyInfo.setReplyTime(this.getReplyTime());
        return replyInfo;
    }
}
