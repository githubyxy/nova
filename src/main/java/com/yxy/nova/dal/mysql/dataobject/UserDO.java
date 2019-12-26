package com.yxy.nova.dal.mysql.dataobject;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "user")
public class UserDO implements Serializable {
    /**
     * 主键(自增ID)
     */
    @Id
    private Long id;

    /**
     * 用户id
     */
    @Column(name = "user_uuid")
    private String userUuid;

    /**
     * 登录账号
     */
    @Column(name = "login_id")
    private String loginId;

    /**
     * 工号
     */
    @Column(name = "employee_no")
    private String employeeNo;

    /**
     * 真实姓名
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 合作方编号, 同盾用户此字段为空
     */
    @Column(name = "partner_code")
    private String partnerCode;

    /**
     * 所属部门编号
     */
    @Column(name = "department_uuid")
    private String departmentUuid;

    /**
     * 联系电话
     */
    @Column(name = "contact_tel")
    private String contactTel;

    /**
     * 坐席号码
     */
    @Column(name = "agent_tel")
    private String agentTel;

    /**
     * 呼叫类型（枚举：VIRTUAL_CALL:网络电话；BIDIRECTIONAL_CALL:双向呼叫）
     */
    @Column(name = "call_type")
    private String callType;

    /**
     * 网络号码
     */
    @Column(name = "virtual_tel")
    private String virtualTel;

    /**
     * 备用号码
     */
    @Column(name = "standby_tel")
    private String standbyTel;

    /**
     * 状态
     */
    private String status;

    /**
     * 所属角色，不同角色用逗号分隔
     */
    @Column(name = "role_uuid")
    private String roleUuid;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

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
     * 管理的合作方编码，用逗号分隔, 只有同盾运营账号可能有值
     */
    @Column(name = "owned_partner")
    private String ownedPartner;

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
     * 获取用户id
     *
     * @return user_uuid - 用户id
     */
    public String getUserUuid() {
        return userUuid;
    }

    /**
     * 设置用户id
     *
     * @param userUuid 用户id
     */
    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid == null ? null : userUuid.trim();
    }

    /**
     * 获取登录账号
     *
     * @return login_id - 登录账号
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * 设置登录账号
     *
     * @param loginId 登录账号
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId == null ? null : loginId.trim();
    }

    /**
     * 获取工号
     *
     * @return employee_no - 工号
     */
    public String getEmployeeNo() {
        return employeeNo;
    }

    /**
     * 设置工号
     *
     * @param employeeNo 工号
     */
    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo == null ? null : employeeNo.trim();
    }

    /**
     * 获取真实姓名
     *
     * @return real_name - 真实姓名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置真实姓名
     *
     * @param realName 真实姓名
     */
    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    /**
     * 获取合作方编号, 同盾用户此字段为空
     *
     * @return partner_code - 合作方编号, 同盾用户此字段为空
     */
    public String getPartnerCode() {
        return partnerCode;
    }

    /**
     * 设置合作方编号, 同盾用户此字段为空
     *
     * @param partnerCode 合作方编号, 同盾用户此字段为空
     */
    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode == null ? null : partnerCode.trim();
    }

    /**
     * 获取所属部门编号
     *
     * @return department_uuid - 所属部门编号
     */
    public String getDepartmentUuid() {
        return departmentUuid;
    }

    /**
     * 设置所属部门编号
     *
     * @param departmentUuid 所属部门编号
     */
    public void setDepartmentUuid(String departmentUuid) {
        this.departmentUuid = departmentUuid == null ? null : departmentUuid.trim();
    }

    /**
     * 获取联系电话
     *
     * @return contact_tel - 联系电话
     */
    public String getContactTel() {
        return contactTel;
    }

    /**
     * 设置联系电话
     *
     * @param contactTel 联系电话
     */
    public void setContactTel(String contactTel) {
        this.contactTel = contactTel == null ? null : contactTel.trim();
    }

    /**
     * 获取坐席号码
     *
     * @return agent_tel - 坐席号码
     */
    public String getAgentTel() {
        return agentTel;
    }

    /**
     * 设置坐席号码
     *
     * @param agentTel 坐席号码
     */
    public void setAgentTel(String agentTel) {
        this.agentTel = agentTel == null ? null : agentTel.trim();
    }

    /**
     * 获取呼叫类型（枚举：VIRTUAL_CALL:网络电话；BIDIRECTIONAL_CALL:双向呼叫）
     *
     * @return call_type - 呼叫类型（枚举：VIRTUAL_CALL:网络电话；BIDIRECTIONAL_CALL:双向呼叫）
     */
    public String getCallType() {
        return callType;
    }

    /**
     * 设置呼叫类型（枚举：VIRTUAL_CALL:网络电话；BIDIRECTIONAL_CALL:双向呼叫）
     *
     * @param callType 呼叫类型（枚举：VIRTUAL_CALL:网络电话；BIDIRECTIONAL_CALL:双向呼叫）
     */
    public void setCallType(String callType) {
        this.callType = callType == null ? null : callType.trim();
    }

    /**
     * 获取网络号码
     *
     * @return virtual_tel - 网络号码
     */
    public String getVirtualTel() {
        return virtualTel;
    }

    /**
     * 设置网络号码
     *
     * @param virtualTel 网络号码
     */
    public void setVirtualTel(String virtualTel) {
        this.virtualTel = virtualTel == null ? null : virtualTel.trim();
    }

    /**
     * 获取备用号码
     *
     * @return standby_tel - 备用号码
     */
    public String getStandbyTel() {
        return standbyTel;
    }

    /**
     * 设置备用号码
     *
     * @param standbyTel 备用号码
     */
    public void setStandbyTel(String standbyTel) {
        this.standbyTel = standbyTel == null ? null : standbyTel.trim();
    }

    /**
     * 获取状态
     *
     * @return status - 状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * 获取所属角色，不同角色用逗号分隔
     *
     * @return role_uuid - 所属角色，不同角色用逗号分隔
     */
    public String getRoleUuid() {
        return roleUuid;
    }

    /**
     * 设置所属角色，不同角色用逗号分隔
     *
     * @param roleUuid 所属角色，不同角色用逗号分隔
     */
    public void setRoleUuid(String roleUuid) {
        this.roleUuid = roleUuid == null ? null : roleUuid.trim();
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 获取盐
     *
     * @return salt - 盐
     */
    public String getSalt() {
        return salt;
    }

    /**
     * 设置盐
     *
     * @param salt 盐
     */
    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
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
     * 获取管理的合作方编码，用逗号分隔, 只有同盾运营账号可能有值
     *
     * @return owned_partner - 管理的合作方编码，用逗号分隔, 只有同盾运营账号可能有值
     */
    public String getOwnedPartner() {
        return ownedPartner;
    }

    /**
     * 设置管理的合作方编码，用逗号分隔, 只有同盾运营账号可能有值
     *
     * @param ownedPartner 管理的合作方编码，用逗号分隔, 只有同盾运营账号可能有值
     */
    public void setOwnedPartner(String ownedPartner) {
        this.ownedPartner = ownedPartner == null ? null : ownedPartner.trim();
    }
}