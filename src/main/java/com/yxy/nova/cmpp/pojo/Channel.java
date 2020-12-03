package com.yxy.nova.cmpp.pojo;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by erqing.zhang on 2017/7/20.
 */
public class Channel {
    private Long id;
    private Integer type;
    private String channelName;
    private String name;
    private String password;
    private String url;
    private String sign;
    private int status;
    private boolean supportUp;
    private JSONObject capacity;
    private String workingStartTime;
    private String workingEndTime;
    private String desc;
    private String froms;
    private Integer channelLoadBalance=1;
    /**
     * 通道敏感词
     */
    private String[] channelSensitiveWords;
    /**
     * 服务商
     */
    private String smsOperator;
    /**
     * 退订信息
     */
    private String unsubscribeInfo;

    /**
     * 脚本ID
     */
    private Long   scriptId;

    private boolean reportFlag = false;
    private boolean replyFlag = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSupportUp() {
        return supportUp;
    }

    public void setSupportUp(boolean supportUp) {
        this.supportUp = supportUp;
    }

    public JSONObject getCapacity() {
        return capacity;
    }

    public void setCapacity(JSONObject capacity) {
        this.capacity = capacity;
    }

    public String getWorkingStartTime() {
        return workingStartTime;
    }

    public void setWorkingStartTime(String workingStartTime) {
        this.workingStartTime = workingStartTime;
    }

    public String getWorkingEndTime() {
        return workingEndTime;
    }

    public void setWorkingEndTime(String workingEndTime) {
        this.workingEndTime = workingEndTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFroms() {
        return froms;
    }

    public void setFroms(String froms) {
        this.froms = froms;
    }

    public String getSmsOperator() {
        return smsOperator;
    }

    public void setSmsOperator(String smsOperator) {
        this.smsOperator = smsOperator;
    }

    public String getUnsubscribeInfo() {
        return unsubscribeInfo;
    }

    public void setUnsubscribeInfo(String unsubscribeInfo) {
        this.unsubscribeInfo = unsubscribeInfo;
    }

    public Long getScriptId() {
        return scriptId;
    }

    public void setScriptId(Long scriptId) {
        this.scriptId = scriptId;
    }

    /**
     * 该渠道是否支持传入的运营商
     *
     * @param operator
     * @return
     */
    public boolean isSupportOperator(String operator) {
        return (boolean) capacity.get(operator);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public boolean isReportFlag() {
        return reportFlag;
    }

    public void setReportFlag(boolean reportFlag) {
        this.reportFlag = reportFlag;
    }

    public boolean isReplyFlag() {
        return replyFlag;
    }

    public void setReplyFlag(boolean replyFlag) {
        this.replyFlag = replyFlag;
    }

    public Integer getChannelLoadBalance() {
        return channelLoadBalance;
    }

    public void setChannelLoadBalance(Integer channelLoadBalance) {
        this.channelLoadBalance = channelLoadBalance;
    }

    public String[] getChannelSensitiveWords() {
        return channelSensitiveWords;
    }

    public void setChannelSensitiveWords(String[] channelSensitiveWords) {
        this.channelSensitiveWords = channelSensitiveWords;
    }
}
