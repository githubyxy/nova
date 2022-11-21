package com.yxy.nova.dal.mysql.dataobject;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "task_item_exec_call")
public class TaskItemExecCallDO implements Serializable {
    /**
     * 主键(自增ID)
     */
    @Id
    private Long id;

    /**
     * 数据唯一id
     */
    @Column(name = "task_item_exec_uuid")
    private String taskItemExecUuid;

    /**
     * 关联task_item表
     */
    @Column(name = "task_item_uuid")
    private String taskItemUuid;

    /**
     * 策略uuid
     */
    @Column(name = "policy_uuid")
    private String policyUuid;

    /**
     * 任务批次uuid
     */
    @Column(name = "task_batch_uuid")
    private Integer taskBatchUuid;

    /**
     * task表的主键
     */
    @Column(name = "task_uuid")
    private String taskUuid;

    /**
     * 合作方编号
     */
    @Column(name = "partner_code")
    private String partnerCode;

    /**
     * 应用编码
     */
    @Column(name = "app_code")
    private String appCode;

    /**
     * 外呼开始时间
     */
    @Column(name = "call_start_time")
    private Date callStartTime;

    /**
     * 机器人通话时长，单位：秒
     */
    @Column(name = "robot_duration")
    private Integer robotDuration;

    /**
     * 机器人通话时长，单位：秒
     */
    @Column(name = "customer_duration")
    private Integer customerDuration;

    /**
     * 机器人电话
     */
    @Column(name = "robot_phone")
    private String robotPhone;

    /**
     * 客户电话
     */
    @Column(name = "customer_phone")
    private String customerPhone;

    /**
     * 机器人通话状态
     */
    @Column(name = "robot_result_code")
    private String robotResultCode;

    /**
     * 机器人通话状态描述
     */
    @Column(name = "robot_result_desc")
    private String robotResultDesc;

    /**
     * 客户通话状态
     */
    @Column(name = "customer_result_code")
    private String customerResultCode;

    /**
     * 客户通话状态描述
     */
    @Column(name = "customer_result_desc")
    private String customerResultDesc;

    /**
     * 状态：待外呼/外呼调度中/外呼完成/过期未调度/调度失败/取消调度
     */
    private String status;

    /**
     * 录音文件id
     */
    @Column(name = "record_file_id")
    private String recordFileId;

    /**
     * 主叫号码
     */
    private String caller;

    /**
     * 主叫号码所在地
     */
    @Column(name = "caller_location")
    private String callerLocation;

    /**
     * 自动化结果编码
     */
    @Column(name = "tag_code")
    private String tagCode;

    /**
     * 自动化结果描述
     */
    @Column(name = "tag_desc")
    private String tagDesc;

    /**
     * 外呼推送时间
     */
    @Column(name = "call_push_time")
    private Date callPushTime;

    /**
     * 允许推送的最早时间
     */
    @Column(name = "next_push_time")
    private Date nextPushTime;

    /**
     * 是否被中止
     */
    @Column(name = "is_halted")
    private Boolean isHalted;

    /**
     * 话术类型（IVR、AI
     */
    @Column(name = "talk_type")
    private String talkType;

    /**
     * 提供AI话术的服务商
     */
    @Column(name = "talk_service_provider")
    private String talkServiceProvider;

    /**
     * 备注
     */
    private String comment;

    @Column(name = "ivr_key")
    private String ivrKey;

    /**
     * varys只能在此时间前外呼，过了这个时间不允许再外呼
     */
    private Date deadline;

    /**
     * 普通标签
     */
    @Column(name = "tag_list")
    private String tagList;

    /**
     * 对话轮次
     */
    @Column(name = "dialogue_round")
    private Integer dialogueRound;

    /**
     * 客户是否中途挂机
     */
    @Column(name = "customer_hangup")
    private Boolean customerHangup;

    /**
     * 匹配的意向标签规则
     */
    @Column(name = "matched_intention_tag_rule")
    private String matchedIntentionTagRule;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modify")
    private Date gmtModify;

    /**
     * 交互详情
     */
    @Column(name = "interaction_detail")
    private String interactionDetail;

    private static final long serialVersionUID = 1L;

    /**
     * 获取主键(自增ID)
     *
     * @return id - 主键(自增ID)
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键(自增ID)
     *
     * @param id 主键(自增ID)
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取数据唯一id
     *
     * @return task_item_exec_uuid - 数据唯一id
     */
    public String getTaskItemExecUuid() {
        return taskItemExecUuid;
    }

    /**
     * 设置数据唯一id
     *
     * @param taskItemExecUuid 数据唯一id
     */
    public void setTaskItemExecUuid(String taskItemExecUuid) {
        this.taskItemExecUuid = taskItemExecUuid == null ? null : taskItemExecUuid.trim();
    }

    /**
     * 获取关联task_item表
     *
     * @return task_item_uuid - 关联task_item表
     */
    public String getTaskItemUuid() {
        return taskItemUuid;
    }

    /**
     * 设置关联task_item表
     *
     * @param taskItemUuid 关联task_item表
     */
    public void setTaskItemUuid(String taskItemUuid) {
        this.taskItemUuid = taskItemUuid == null ? null : taskItemUuid.trim();
    }

    /**
     * 获取策略uuid
     *
     * @return policy_uuid - 策略uuid
     */
    public String getPolicyUuid() {
        return policyUuid;
    }

    /**
     * 设置策略uuid
     *
     * @param policyUuid 策略uuid
     */
    public void setPolicyUuid(String policyUuid) {
        this.policyUuid = policyUuid == null ? null : policyUuid.trim();
    }

    /**
     * 获取任务批次uuid
     *
     * @return task_batch_uuid - 任务批次uuid
     */
    public Integer getTaskBatchUuid() {
        return taskBatchUuid;
    }

    /**
     * 设置任务批次uuid
     *
     * @param taskBatchUuid 任务批次uuid
     */
    public void setTaskBatchUuid(Integer taskBatchUuid) {
        this.taskBatchUuid = taskBatchUuid;
    }

    /**
     * 获取task表的主键
     *
     * @return task_uuid - task表的主键
     */
    public String getTaskUuid() {
        return taskUuid;
    }

    /**
     * 设置task表的主键
     *
     * @param taskUuid task表的主键
     */
    public void setTaskUuid(String taskUuid) {
        this.taskUuid = taskUuid == null ? null : taskUuid.trim();
    }

    /**
     * 获取合作方编号
     *
     * @return partner_code - 合作方编号
     */
    public String getPartnerCode() {
        return partnerCode;
    }

    /**
     * 设置合作方编号
     *
     * @param partnerCode 合作方编号
     */
    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode == null ? null : partnerCode.trim();
    }

    /**
     * 获取应用编码
     *
     * @return app_code - 应用编码
     */
    public String getAppCode() {
        return appCode;
    }

    /**
     * 设置应用编码
     *
     * @param appCode 应用编码
     */
    public void setAppCode(String appCode) {
        this.appCode = appCode == null ? null : appCode.trim();
    }

    /**
     * 获取外呼开始时间
     *
     * @return call_start_time - 外呼开始时间
     */
    public Date getCallStartTime() {
        return callStartTime;
    }

    /**
     * 设置外呼开始时间
     *
     * @param callStartTime 外呼开始时间
     */
    public void setCallStartTime(Date callStartTime) {
        this.callStartTime = callStartTime;
    }

    /**
     * 获取机器人通话时长，单位：秒
     *
     * @return robot_duration - 机器人通话时长，单位：秒
     */
    public Integer getRobotDuration() {
        return robotDuration;
    }

    /**
     * 设置机器人通话时长，单位：秒
     *
     * @param robotDuration 机器人通话时长，单位：秒
     */
    public void setRobotDuration(Integer robotDuration) {
        this.robotDuration = robotDuration;
    }

    /**
     * 获取机器人通话时长，单位：秒
     *
     * @return customer_duration - 机器人通话时长，单位：秒
     */
    public Integer getCustomerDuration() {
        return customerDuration;
    }

    /**
     * 设置机器人通话时长，单位：秒
     *
     * @param customerDuration 机器人通话时长，单位：秒
     */
    public void setCustomerDuration(Integer customerDuration) {
        this.customerDuration = customerDuration;
    }

    /**
     * 获取机器人电话
     *
     * @return robot_phone - 机器人电话
     */
    public String getRobotPhone() {
        return robotPhone;
    }

    /**
     * 设置机器人电话
     *
     * @param robotPhone 机器人电话
     */
    public void setRobotPhone(String robotPhone) {
        this.robotPhone = robotPhone == null ? null : robotPhone.trim();
    }

    /**
     * 获取客户电话
     *
     * @return customer_phone - 客户电话
     */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * 设置客户电话
     *
     * @param customerPhone 客户电话
     */
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone == null ? null : customerPhone.trim();
    }

    /**
     * 获取机器人通话状态
     *
     * @return robot_result_code - 机器人通话状态
     */
    public String getRobotResultCode() {
        return robotResultCode;
    }

    /**
     * 设置机器人通话状态
     *
     * @param robotResultCode 机器人通话状态
     */
    public void setRobotResultCode(String robotResultCode) {
        this.robotResultCode = robotResultCode == null ? null : robotResultCode.trim();
    }

    /**
     * 获取机器人通话状态描述
     *
     * @return robot_result_desc - 机器人通话状态描述
     */
    public String getRobotResultDesc() {
        return robotResultDesc;
    }

    /**
     * 设置机器人通话状态描述
     *
     * @param robotResultDesc 机器人通话状态描述
     */
    public void setRobotResultDesc(String robotResultDesc) {
        this.robotResultDesc = robotResultDesc == null ? null : robotResultDesc.trim();
    }

    /**
     * 获取客户通话状态
     *
     * @return customer_result_code - 客户通话状态
     */
    public String getCustomerResultCode() {
        return customerResultCode;
    }

    /**
     * 设置客户通话状态
     *
     * @param customerResultCode 客户通话状态
     */
    public void setCustomerResultCode(String customerResultCode) {
        this.customerResultCode = customerResultCode == null ? null : customerResultCode.trim();
    }

    /**
     * 获取客户通话状态描述
     *
     * @return customer_result_desc - 客户通话状态描述
     */
    public String getCustomerResultDesc() {
        return customerResultDesc;
    }

    /**
     * 设置客户通话状态描述
     *
     * @param customerResultDesc 客户通话状态描述
     */
    public void setCustomerResultDesc(String customerResultDesc) {
        this.customerResultDesc = customerResultDesc == null ? null : customerResultDesc.trim();
    }

    /**
     * 获取状态：待外呼/外呼调度中/外呼完成/过期未调度/调度失败/取消调度
     *
     * @return status - 状态：待外呼/外呼调度中/外呼完成/过期未调度/调度失败/取消调度
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态：待外呼/外呼调度中/外呼完成/过期未调度/调度失败/取消调度
     *
     * @param status 状态：待外呼/外呼调度中/外呼完成/过期未调度/调度失败/取消调度
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * 获取录音文件id
     *
     * @return record_file_id - 录音文件id
     */
    public String getRecordFileId() {
        return recordFileId;
    }

    /**
     * 设置录音文件id
     *
     * @param recordFileId 录音文件id
     */
    public void setRecordFileId(String recordFileId) {
        this.recordFileId = recordFileId == null ? null : recordFileId.trim();
    }

    /**
     * 获取主叫号码
     *
     * @return caller - 主叫号码
     */
    public String getCaller() {
        return caller;
    }

    /**
     * 设置主叫号码
     *
     * @param caller 主叫号码
     */
    public void setCaller(String caller) {
        this.caller = caller == null ? null : caller.trim();
    }

    /**
     * 获取主叫号码所在地
     *
     * @return caller_location - 主叫号码所在地
     */
    public String getCallerLocation() {
        return callerLocation;
    }

    /**
     * 设置主叫号码所在地
     *
     * @param callerLocation 主叫号码所在地
     */
    public void setCallerLocation(String callerLocation) {
        this.callerLocation = callerLocation == null ? null : callerLocation.trim();
    }

    /**
     * 获取自动化结果编码
     *
     * @return tag_code - 自动化结果编码
     */
    public String getTagCode() {
        return tagCode;
    }

    /**
     * 设置自动化结果编码
     *
     * @param tagCode 自动化结果编码
     */
    public void setTagCode(String tagCode) {
        this.tagCode = tagCode == null ? null : tagCode.trim();
    }

    /**
     * 获取自动化结果描述
     *
     * @return tag_desc - 自动化结果描述
     */
    public String getTagDesc() {
        return tagDesc;
    }

    /**
     * 设置自动化结果描述
     *
     * @param tagDesc 自动化结果描述
     */
    public void setTagDesc(String tagDesc) {
        this.tagDesc = tagDesc == null ? null : tagDesc.trim();
    }

    /**
     * 获取外呼推送时间
     *
     * @return call_push_time - 外呼推送时间
     */
    public Date getCallPushTime() {
        return callPushTime;
    }

    /**
     * 设置外呼推送时间
     *
     * @param callPushTime 外呼推送时间
     */
    public void setCallPushTime(Date callPushTime) {
        this.callPushTime = callPushTime;
    }

    /**
     * 获取允许推送的最早时间
     *
     * @return next_push_time - 允许推送的最早时间
     */
    public Date getNextPushTime() {
        return nextPushTime;
    }

    /**
     * 设置允许推送的最早时间
     *
     * @param nextPushTime 允许推送的最早时间
     */
    public void setNextPushTime(Date nextPushTime) {
        this.nextPushTime = nextPushTime;
    }

    /**
     * 获取是否被中止
     *
     * @return is_halted - 是否被中止
     */
    public Boolean getIsHalted() {
        return isHalted;
    }

    /**
     * 设置是否被中止
     *
     * @param isHalted 是否被中止
     */
    public void setIsHalted(Boolean isHalted) {
        this.isHalted = isHalted;
    }

    /**
     * 获取话术类型（IVR、AI
     *
     * @return talk_type - 话术类型（IVR、AI
     */
    public String getTalkType() {
        return talkType;
    }

    /**
     * 设置话术类型（IVR、AI
     *
     * @param talkType 话术类型（IVR、AI
     */
    public void setTalkType(String talkType) {
        this.talkType = talkType == null ? null : talkType.trim();
    }

    /**
     * 获取提供AI话术的服务商
     *
     * @return talk_service_provider - 提供AI话术的服务商
     */
    public String getTalkServiceProvider() {
        return talkServiceProvider;
    }

    /**
     * 设置提供AI话术的服务商
     *
     * @param talkServiceProvider 提供AI话术的服务商
     */
    public void setTalkServiceProvider(String talkServiceProvider) {
        this.talkServiceProvider = talkServiceProvider == null ? null : talkServiceProvider.trim();
    }

    /**
     * 获取备注
     *
     * @return comment - 备注
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置备注
     *
     * @param comment 备注
     */
    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    /**
     * @return ivr_key
     */
    public String getIvrKey() {
        return ivrKey;
    }

    /**
     * @param ivrKey
     */
    public void setIvrKey(String ivrKey) {
        this.ivrKey = ivrKey == null ? null : ivrKey.trim();
    }

    /**
     * 获取varys只能在此时间前外呼，过了这个时间不允许再外呼
     *
     * @return deadline - varys只能在此时间前外呼，过了这个时间不允许再外呼
     */
    public Date getDeadline() {
        return deadline;
    }

    /**
     * 设置varys只能在此时间前外呼，过了这个时间不允许再外呼
     *
     * @param deadline varys只能在此时间前外呼，过了这个时间不允许再外呼
     */
    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    /**
     * 获取普通标签
     *
     * @return tag_list - 普通标签
     */
    public String getTagList() {
        return tagList;
    }

    /**
     * 设置普通标签
     *
     * @param tagList 普通标签
     */
    public void setTagList(String tagList) {
        this.tagList = tagList == null ? null : tagList.trim();
    }

    /**
     * 获取对话轮次
     *
     * @return dialogue_round - 对话轮次
     */
    public Integer getDialogueRound() {
        return dialogueRound;
    }

    /**
     * 设置对话轮次
     *
     * @param dialogueRound 对话轮次
     */
    public void setDialogueRound(Integer dialogueRound) {
        this.dialogueRound = dialogueRound;
    }

    /**
     * 获取客户是否中途挂机
     *
     * @return customer_hangup - 客户是否中途挂机
     */
    public Boolean getCustomerHangup() {
        return customerHangup;
    }

    /**
     * 设置客户是否中途挂机
     *
     * @param customerHangup 客户是否中途挂机
     */
    public void setCustomerHangup(Boolean customerHangup) {
        this.customerHangup = customerHangup;
    }

    /**
     * 获取匹配的意向标签规则
     *
     * @return matched_intention_tag_rule - 匹配的意向标签规则
     */
    public String getMatchedIntentionTagRule() {
        return matchedIntentionTagRule;
    }

    /**
     * 设置匹配的意向标签规则
     *
     * @param matchedIntentionTagRule 匹配的意向标签规则
     */
    public void setMatchedIntentionTagRule(String matchedIntentionTagRule) {
        this.matchedIntentionTagRule = matchedIntentionTagRule == null ? null : matchedIntentionTagRule.trim();
    }

    /**
     * 获取创建时间
     *
     * @return gmt_create - 创建时间
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 设置创建时间
     *
     * @param gmtCreate 创建时间
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 获取修改时间
     *
     * @return gmt_modify - 修改时间
     */
    public Date getGmtModify() {
        return gmtModify;
    }

    /**
     * 设置修改时间
     *
     * @param gmtModify 修改时间
     */
    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    /**
     * 获取交互详情
     *
     * @return interaction_detail - 交互详情
     */
    public String getInteractionDetail() {
        return interactionDetail;
    }

    /**
     * 设置交互详情
     *
     * @param interactionDetail 交互详情
     */
    public void setInteractionDetail(String interactionDetail) {
        this.interactionDetail = interactionDetail == null ? null : interactionDetail.trim();
    }
}