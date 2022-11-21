package com.yxy.nova.mwh.elasticsearch.exception;


public enum ESExceptionType implements ExceptionCode{
    POLICY_NULL(72002,"没有定义策略"),
    POLICY_ERROR(72003,"策略解析错误"),
    CONNECT_ERROR(72004,"建立链接错误"),
    STREAM_ERROR(72005,"处理输入输出流失败"),
    RESPONSE_ERROR(72006,"处理请求响应失败"),
    REQUEST_ERROR(72007,"处理请求失败"),
    INDEX_ERROR(72008,"获取索引失败"),
    RANGE_ERROR(72009,"查询跨度太大"),
    CACHE_ERROR(72010,"获取缓存信息失败"),
    PARTITION_ERROR(72011,"分区索引获取错误"),
    CONDITION_ERROR(72012,"查询条件不正确"),
    METHOD_ERROR(72013,"方法使用错误"),
    FLUSH_ERROR(72014,"缓存刷新失败"),
    QUERY_ERROR(72015,"查询数据失败"),
    SAVE_ERROR(72016,"保存数据失败"),
    PARAM_ERROR(72017,"参数错误"),
    HTTP_REQUEST_ERROR(72018,"http的请求错误"),
    HTTP_BUILD_REQUEST_ERROR(72019,"http请求地址错误"),
    HTTP_URL_ERROR(72020,"http请求URL信息错误"),
    HTTP_RESPONSE_ERROR(72021,"http的response处理错误"),
    HTTP_BODY_ENCODING_ERROR(72022,"http的请求体编码处理错误"),
    AGGREGATION_BUCKET_ERROR(72023,"聚合查询分桶错误"),
    AGGREGATION_METRIC_ERROR(72024,"聚合查询计算指标错误"),;

    private int code;
    private String desc;
    private ESExceptionType(int code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
}
