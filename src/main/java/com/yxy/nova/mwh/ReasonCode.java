package com.yxy.nova.mwh;

/**
 * 新版状态码对外5000-5999，参考http://wiki.tongdun.me/pages/viewpage.action?pageId=30541771
 * 规定如下：
 * 51XX 参数问题
 * 53XX 流量问题
 * 54XX 资源问题
 * 55XX 系统问题
 * 5600-5649 interaction-bridge应用专用
 * 5650-5699 robot-engine应用专用
 * 5700-5999 业务问题,可进一步分类,按应用抢占(最少50个,最多100个)
 */
public enum ReasonCode {

    SUCCESS("200", "成功"),

    /**
     * 权限类
     */
    AUTH_FAILED("001", "用户认证失败"), // 权限类, 错误码和描述是UIG统一定义的
    ACCESS_DENIED("600", "访问受限, 限流"), // 权限类, 错误码和描述是UIG统一定义的
    ACCESS_DENIED1("610", "访问受限，白名单"), // 权限类, 错误码和描述是UIG统一定义的


    /**
     * 参数类
     */
    PARAM_ERROR("5100", "参数错误"), // 参数问题总类，无法定义的可以使用该码，优先使用具体错误码
    PARAM_MISS("5101", "参数缺失"),
    PARAM_INVALID("5102", "参数非法"),
    NO_PARTNERCODE("5103", "请选择合作方"),
    NO_APPCODE("5104", "请选择应用"),
    REPEATED_REQUEST("5105", "重复的请求"),

    /**
     * 流量类
     */
    FLOW_ERROR("5300", "流量错误"), // 流量问题总类，无法定义的可以使用该码，优先使用具体错误码
    FLOW_UNAUTHORIZED("5301", "服务未购买"),
    FLOW_FORBIDDEN("5302", "服务已被禁用"),
    FLOW_GONE("5303", "流量不足"),
    FLOW_EXPIRED("5304", "服务已过期"),

    /**
     * 资源类
     */
    RESOURCE_ERROR("5400", "资源问题"), // 资源问题总类，无法定义的可以使用该码，优先使用具体错误码
    RESOURCE_UNAUTHORIZED("5401", "资源未授权"),
    RESOURCE_FORBIDDEN("5402", "资源被禁用"),
    RESOURCE_NOT_FOUND("5403", "资源不存在"),
    RESOURCE_EXPIRED("5404", "资源已过期"),

    /**
     * 系统类
     */
    SYS_ERROR("5500", "系统内部错误"), // 系统问题总类，无法定义的可以使用该码，优先使用具体错误码
    SYS_TIMEOUT("5501", "系统处理超时"),
    SYS_EXCEPTION("5502", "系统处理异常"),
    SYS_LIMIT_PROTECT("5503","系统限流保护"),
    SYS_FUSE_PROTECT("5504", "系统熔断保护"),
    SYS_DEGRADE_PROTECT("5505", "系统降级保护"),
    SYS_UNAVAILABLE("5506", "系统服务不可用"),

    /**
     * interaction-bridge应用专有类
     */
    NO_DATA_PROVIDER("5600", "未配置数据提供者"),
    DATA_PROVIDER_ERROR("5601", "数据提供者配置错误"),
    PROVIDER_INVOKE_EXCEPTION("5602", "提供者调用异常"),
    PROVIDER_RESULT_ERROR("5603", "提供者结果错误"),

    /**
     * robot-engine应用专有类
     */
    TALKID_NOT_FOUND("5650", "话术ID不存在"),
    TALKUUID_NOT_FOUND("5651", "话术UUID不存在"),
    NODEID_NOT_FOUND("5652", "节点ID不存在"),
    KNOWLEDGE_QUESTION_NOT_FOUND("5653", "知识库问题不存在"),
    FALLBACK_POLICY_NOT_FOUND("5654", "兜底策略不存在"),
    TALK_CONFIG_INVALID("5655", "话术配置非法"),
    TALK_WARM_EXCEPTION("5656", "话术预热异常"),
    CALLCONTEXT_EXCEPTION("5657", "通话上下文异常"),
    NODECONTEXT_EXCEPTION("5658", "节点上下文异常"),
    RECORD_FILE_STREAM_GET_FAILD("5659", "获取语音文件流失败"),

    /**
     * 短信专有类
     */
    MOBILE_NOT_NULL("5720", "手机号不能为空"),
    MOBILE_ERROR("5721","手机号格式错误"),
    CONTENT_NOT_NULL("5730","短息内容不能为空"),
    CONTENT_LENGTH_ILLEGAL("5731","短息内容长度超出"),
    OUT_ID_ERROR("5740","非法的自定义out_id"),
    EXT_ERROR("5750","扩展号非法"),
    SCHEDULE_TIME_ERROR("5751","短信定时发送时间非法"),
    TEMPLATE_CANT_USE("5753","模板不可用"),

    ;
    private String code;
    private String desc;

    ReasonCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
