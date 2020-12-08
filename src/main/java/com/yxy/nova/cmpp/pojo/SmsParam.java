package com.yxy.nova.cmpp.pojo;//package com.yxy.nova.cmpp.pojo;
//
//import com.alibaba.fastjson.JSONObject;
//import org.apache.commons.lang3.builder.ToStringBuilder;
//
//import javax.ws.rs.FormParam;
//import java.io.Serializable;
//
///**
// * Created by erqing.zhang on 2017/7/18.
// *
// * 发送短信参数
// */
//public class SmsParam implements Serializable {
//    /**
//     * 配置ID
//     */
//    @FormParam("config_id")
//    private Long configId;
//
//    /**
//     * 渠道ID
//     */
//    @FormParam("channel_id")
//    private Long channelId;
//
//    @FormParam("task_id")
//    private Long taskId;
//
//    /**
//     * 接收方手机号码
//     */
//    @FormParam("mobile")
//    private String mobile;
//
//    /**
//     * 发送短信内容
//     */
//    @FormParam("content")
//    private String content;
//
//    /**
//     * 调用方应用名
//     */
//    @FormParam("app_name")
//    private String appName;
//    /**
//     * 合作方名称
//     */
//    @FormParam("partner_code")
//    private String partnerCode;
//    /**
//     * 合作方名称
//     */
//    @FormParam("partner_app_name")
//    private String partnerAppName;
//    /**
//     * 业务方标识码，目前主要用于kafka topic拼接
//     */
//    @FormParam("biz_code")
//    private String bizCode = ConfigConstant.DEFAULT_BIZCODE;
//
//    /**
//     * 调用方扩展字段
//     */
//    private JSONObject ext;
//
//    public Long getConfigId() {
//        return configId;
//    }
//
//    public void setConfigId(Long configId) {
//        this.configId = configId;
//    }
//
//    public String getMobile() {
//        return mobile;
//    }
//
//    public void setMobile(String mobile) {
//        this.mobile = mobile;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public Long getChannelId() {
//        return channelId;
//    }
//
//    public void setChannelId(Long channelId) {
//        this.channelId = channelId;
//    }
//
//    public String getAppName() {
//        return appName;
//    }
//
//    public void setAppName(String appName) {
//        this.appName = appName;
//    }
//
//    public String getBizCode() {
//        return bizCode;
//    }
//
//    public void setBizCode(String bizCode) {
//        this.bizCode = bizCode;
//    }
//
//    public JSONObject getExt() {
//        return ext;
//    }
//
//    public void setExt(JSONObject ext) {
//        this.ext = ext;
//    }
//
//    @Override
//    public String toString() {
//        return ToStringBuilder.reflectionToString(this);
//    }
//
//    public Long getTaskId() {
//        return taskId;
//    }
//
//    public void setTaskId(Long taskId) {
//        this.taskId = taskId;
//    }
//
//    public String getPartnerCode() {
//        return partnerCode;
//    }
//
//    public void setPartnerCode(String partnerCode) {
//        this.partnerCode = partnerCode;
//    }
//
//    public String getPartnerAppName() {
//        return partnerAppName;
//    }
//
//    public void setPartnerAppName(String partnerAppName) {
//        this.partnerAppName = partnerAppName;
//    }
//}
