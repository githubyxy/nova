package com.yxy.nova.bean;

/**
 * @author yuxiaoyu
 * @Description:
 * @date 2019-10-28 14:04
 */
public enum ReasonCode {
    SUCCESS("200", "成功"),
    AUTH_FAILED("001", "用户认证失败"),
    ACCESS_DENIED("600", "访问受限, 限流"),
    ACCESS_DENIED1("610", "访问受限，白名单"),
    PARAM_ERROR("5100", "参数错误"),
    PARAM_MISS("5101", "参数缺失"),
    PARAM_INVALID("5102", "参数非法"),
    NO_PARTNERCODE("5103", "请选择合作方"),
    NO_APPCODE("5104", "请选择应用"),
    REPEATED_REQUEST("5105", "重复的请求"),
    FLOW_ERROR("5300", "流量错误"),
    FLOW_UNAUTHORIZED("5301", "服务未购买"),
    FLOW_FORBIDDEN("5302", "服务已被禁用"),
    FLOW_GONE("5303", "流量不足"),
    FLOW_EXPIRED("5304", "服务已过期"),
    RESOURCE_ERROR("5400", "资源问题"),
    RESOURCE_UNAUTHORIZED("5401", "资源未授权"),
    RESOURCE_FORBIDDEN("5402", "资源被禁用"),
    RESOURCE_NOT_FOUND("5403", "资源不存在"),
    RESOURCE_EXPIRED("5404", "资源已过期"),
    SYS_ERROR("5500", "系统内部错误"),
    SYS_TIMEOUT("5501", "系统处理超时"),
    SYS_EXCEPTION("5502", "系统处理异常"),
    SYS_LIMIT_PROTECT("5503", "系统限流保护"),
    SYS_FUSE_PROTECT("5504", "系统熔断保护"),
    SYS_DEGRADE_PROTECT("5505", "系统降级保护"),
    SYS_UNAVAILABLE("5506", "系统服务不可用"),
    NO_DATA_PROVIDER("5600", "未配置数据提供者"),
    DATA_PROVIDER_ERROR("5601", "数据提供者配置错误"),
    PROVIDER_INVOKE_EXCEPTION("5602", "提供者调用异常"),
    PROVIDER_RESULT_ERROR("5603", "提供者结果错误");

    private String code;
    private String desc;

    private ReasonCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
