package com.yxy.nova.bean.doris;

import java.io.Serializable;

/**
 * @author yxy
 * @description: 通话记录扁平化DTO
 * @date 2023/8/21 3:40 PM
 */
public class CallRecordFlattenedDTO implements Serializable {
    private static final long serialVersionUID = -1038543320983966488L;

    /** 分区字段，格式：yyyy-MM-dd */
    private String ds;

    /** 租户UUID */
    private String tenantUuid;

    /** 通话UUID */
    private String callUuid;

    /** 导航UUID */
    private String navigationUuid;

    /** 队列UUID */
    private String queueUuid;

    /** 座席UUID */
    private String agentUuid;

    /** 关联的通话记录id */
    private String correlatedCallUuid;

    /** 业务类型 */
    private String bizType;

    /** 业务id */
    private String bizId;

    /** 通话方向，如：呼入、呼出 */
    private String callDirection;

    /** 客户号码 */
    private String customerPhone;

    /** 通话状态 */
    private String callStatus;

    /** 查询状态 */
    private String queryStatus;

    /** 通话开始时间 */
    private String callStartTime;

    /** 通话振铃时间 */
    private String callRingTime;

    /** 通话接通时间 */
    private String callAnswerTime;

    /** 通话结束时间 */
    private String callEndTime;

    /** 通话录音文件地址 */
    private String callRecordUrl;

    /** 挂断方 */
    private String hangupBy;

    /** 挂断原因说明 */
    private String hangupReason;

    /** 业务上下文环境 */
    private String context;

    /** 客户跟进状态 */
    private String manualFollowupStatus;

    /**
     * 人工跟进阶段编码
     */
    private String manualFollowupStageCode;

    /**
     * 人工意向标签code
     */
    private String manualTagCode;

    /** 座席小结 */
    private String agentNotes;

    /** 扩展字段 */
    private String extra;

    /** ai执行信息 */
    private String aiInfo;

    /** freeswitch执行结果 */
    private String freeswitchResult;

    /** 回拨分配的座席 */
    private String callbackAgentUuid;

    /** 回拨状态 */
    private String callbackStatus;

    /** 回拨次数 */
    private Integer callbackCount;

    /** 通话是否结束 */
    private Integer finished;

    /** call_record记录的json串 */
    private String detail;

    /** 批次ID */
    private Integer taskBatchUuid;

    /** 客户自定义批次uuid */
    private String customerBatchUuid;

    /** 客户自定义批次名称 */
    private String customerBatchName;

    /** 数据ID */
    private String taskItemUuid;

    /** AI外呼ID */
    private String taskItemExecUuid;

    /** 任务ID */
    private String taskUuid;

    /** 任务名称 */
    private String taskName;

    /** 策略ID */
    private String policyUuid;

    /** ai意向标签code */
    private String aiTagCode;

    /** 话术服务提供商 */
    private String talkServiceProvider;

    /** 变量数据 */
    private String variable;

    /** 合作方编号 */
    private String partnerCode;

    /** 合作方名称 */
    private String partnerDisplayName;

    /** 应用编号 */
    private String appCode;

    /** 应用名称 */
    private String appDisplayName;

    /** 排队总时长 */
    private Long queueDuration;

    /** 振铃总时长 */
    private Long ringDuration;

    /** 人工通话时长 */
    private Long talkDuration;

    /** 整理时长 */
    private Long wrapUpDuration;

    /** 通话时长 */
    private Long duration;

    /** 呼出等待时长 */
    private Long waitDuration;

    /** 版本号 */
    private Integer version;

    /** 创建时间 */
    private String gmtCreate;

    /** 修改时间 */
    private String gmtModify;

    public String getDs() {
        return ds;
    }

    public void setDs(String ds) {
        this.ds = ds;
    }

    public String getTenantUuid() {
        return tenantUuid;
    }

    public void setTenantUuid(String tenantUuid) {
        this.tenantUuid = tenantUuid;
    }

    public String getCallUuid() {
        return callUuid;
    }

    public void setCallUuid(String callUuid) {
        this.callUuid = callUuid;
    }

    public String getNavigationUuid() {
        return navigationUuid;
    }

    public void setNavigationUuid(String navigationUuid) {
        this.navigationUuid = navigationUuid;
    }

    public String getQueueUuid() {
        return queueUuid;
    }

    public void setQueueUuid(String queueUuid) {
        this.queueUuid = queueUuid;
    }

    public String getAgentUuid() {
        return agentUuid;
    }

    public void setAgentUuid(String agentUuid) {
        this.agentUuid = agentUuid;
    }

    public String getCorrelatedCallUuid() {
        return correlatedCallUuid;
    }

    public void setCorrelatedCallUuid(String correlatedCallUuid) {
        this.correlatedCallUuid = correlatedCallUuid;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getCallDirection() {
        return callDirection;
    }

    public void setCallDirection(String callDirection) {
        this.callDirection = callDirection;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(String callStatus) {
        this.callStatus = callStatus;
    }

    public String getQueryStatus() {
        return queryStatus;
    }

    public void setQueryStatus(String queryStatus) {
        this.queryStatus = queryStatus;
    }

    public String getCallStartTime() {
        return callStartTime;
    }

    public void setCallStartTime(String callStartTime) {
        this.callStartTime = callStartTime;
    }

    public String getCallRingTime() {
        return callRingTime;
    }

    public void setCallRingTime(String callRingTime) {
        this.callRingTime = callRingTime;
    }

    public String getCallAnswerTime() {
        return callAnswerTime;
    }

    public void setCallAnswerTime(String callAnswerTime) {
        this.callAnswerTime = callAnswerTime;
    }

    public String getCallEndTime() {
        return callEndTime;
    }

    public void setCallEndTime(String callEndTime) {
        this.callEndTime = callEndTime;
    }

    public String getCallRecordUrl() {
        return callRecordUrl;
    }

    public void setCallRecordUrl(String callRecordUrl) {
        this.callRecordUrl = callRecordUrl;
    }

    public String getHangupBy() {
        return hangupBy;
    }

    public void setHangupBy(String hangupBy) {
        this.hangupBy = hangupBy;
    }

    public String getHangupReason() {
        return hangupReason;
    }

    public void setHangupReason(String hangupReason) {
        this.hangupReason = hangupReason;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getManualFollowupStatus() {
        return manualFollowupStatus;
    }

    public void setManualFollowupStatus(String manualFollowupStatus) {
        this.manualFollowupStatus = manualFollowupStatus;
    }

    public String getAgentNotes() {
        return agentNotes;
    }

    public void setAgentNotes(String agentNotes) {
        this.agentNotes = agentNotes;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getAiInfo() {
        return aiInfo;
    }

    public void setAiInfo(String aiInfo) {
        this.aiInfo = aiInfo;
    }

    public String getFreeswitchResult() {
        return freeswitchResult;
    }

    public void setFreeswitchResult(String freeswitchResult) {
        this.freeswitchResult = freeswitchResult;
    }

    public Integer getFinished() {
        return finished;
    }

    public void setFinished(Integer finished) {
        this.finished = finished;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getTaskBatchUuid() {
        return taskBatchUuid;
    }

    public void setTaskBatchUuid(Integer taskBatchUuid) {
        this.taskBatchUuid = taskBatchUuid;
    }

    public String getCustomerBatchUuid() {
        return customerBatchUuid;
    }

    public void setCustomerBatchUuid(String customerBatchUuid) {
        this.customerBatchUuid = customerBatchUuid;
    }

    public String getCustomerBatchName() {
        return customerBatchName;
    }

    public void setCustomerBatchName(String customerBatchName) {
        this.customerBatchName = customerBatchName;
    }

    public String getTaskItemUuid() {
        return taskItemUuid;
    }

    public void setTaskItemUuid(String taskItemUuid) {
        this.taskItemUuid = taskItemUuid;
    }

    public String getTaskItemExecUuid() {
        return taskItemExecUuid;
    }

    public void setTaskItemExecUuid(String taskItemExecUuid) {
        this.taskItemExecUuid = taskItemExecUuid;
    }

    public String getTaskUuid() {
        return taskUuid;
    }

    public void setTaskUuid(String taskUuid) {
        this.taskUuid = taskUuid;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getPolicyUuid() {
        return policyUuid;
    }

    public void setPolicyUuid(String policyUuid) {
        this.policyUuid = policyUuid;
    }

    public String getAiTagCode() {
        return aiTagCode;
    }

    public void setAiTagCode(String aiTagCode) {
        this.aiTagCode = aiTagCode;
    }

    public String getTalkServiceProvider() {
        return talkServiceProvider;
    }

    public void setTalkServiceProvider(String talkServiceProvider) {
        this.talkServiceProvider = talkServiceProvider;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getPartnerDisplayName() {
        return partnerDisplayName;
    }

    public void setPartnerDisplayName(String partnerDisplayName) {
        this.partnerDisplayName = partnerDisplayName;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppDisplayName() {
        return appDisplayName;
    }

    public void setAppDisplayName(String appDisplayName) {
        this.appDisplayName = appDisplayName;
    }

    public Long getQueueDuration() {
        return queueDuration;
    }

    public void setQueueDuration(Long queueDuration) {
        this.queueDuration = queueDuration;
    }

    public Long getRingDuration() {
        return ringDuration;
    }

    public void setRingDuration(Long ringDuration) {
        this.ringDuration = ringDuration;
    }

    public Long getTalkDuration() {
        return talkDuration;
    }

    public void setTalkDuration(Long talkDuration) {
        this.talkDuration = talkDuration;
    }

    public Long getWrapUpDuration() {
        return wrapUpDuration;
    }

    public void setWrapUpDuration(Long wrapUpDuration) {
        this.wrapUpDuration = wrapUpDuration;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getWaitDuration() {
        return waitDuration;
    }

    public void setWaitDuration(Long waitDuration) {
        this.waitDuration = waitDuration;
    }

    public String getCallbackAgentUuid() {
        return callbackAgentUuid;
    }

    public void setCallbackAgentUuid(String callbackAgentUuid) {
        this.callbackAgentUuid = callbackAgentUuid;
    }

    public String getCallbackStatus() {
        return callbackStatus;
    }

    public void setCallbackStatus(String callbackStatus) {
        this.callbackStatus = callbackStatus;
    }

    public Integer getCallbackCount() {
        return callbackCount;
    }

    public void setCallbackCount(Integer callbackCount) {
        this.callbackCount = callbackCount;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }

    public String getManualFollowupStageCode() {
        return manualFollowupStageCode;
    }

    public void setManualFollowupStageCode(String manualFollowupStageCode) {
        this.manualFollowupStageCode = manualFollowupStageCode;
    }

    public String getManualTagCode() {
        return manualTagCode;
    }

    public void setManualTagCode(String manualTagCode) {
        this.manualTagCode = manualTagCode;
    }
}
