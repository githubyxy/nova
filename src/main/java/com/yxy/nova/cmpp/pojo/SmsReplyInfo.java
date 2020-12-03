package com.yxy.nova.cmpp.pojo;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by lqq on 2017/8/9.
 */
public class SmsReplyInfo implements Serializable {

    private static final long serialVersionUID = -1468939659973168195L;

    private String            msgId;
    private String            mobile;
    private String            content;
    private Timestamp         replyTime;
    private JSONObject ext;

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

    public Timestamp getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Timestamp replyTime) {
        this.replyTime = replyTime;
    }

    public JSONObject getExt() {
        return ext;
    }

    public void setExt(JSONObject ext) {
        this.ext = ext;
    }
}
