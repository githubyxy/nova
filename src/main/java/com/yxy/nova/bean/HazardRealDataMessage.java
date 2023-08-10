package com.yxy.nova.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: yxy
 * @date: 2020-02-18 10:38 上午
 */
@Getter
@Setter
public class HazardRealDataMessage implements Serializable {

    private static final long serialVersionUID = 4054702101546030694L;

    /**
     * 调用者定义的数据包 ID，同一 个数据包必须具有相同且全局 唯一的 ID。服务使用本字段数 据判断是否传递了重复的包。 建议使用 UUID
     */
    private String dataId;
    private String dataType;

    /**
     * 企业编码
     */
    private String enterpriseId;
    /**
     * 网关编码
     */
    private String gatewayId;
    /**
     * 时间戳，格式 yyyyMMddHHmmss 示例:20200701142510
     */
    private String collectTime;
    /**
     * 数据源连通性，true 表示数据 源连通正常，数据有效; false 表示数据源连通异常， 数据无效
     */
    private Boolean isConnectDataSource;
    /**
     * 报文类型，report 表示实时报 文;continues 表示断点续传 的报文
     */
    private String reportType;
    /**
     * 指标数据集合
     */
    private List<RealData> datas;


    @Data
    public static class RealData {
        /**
         * 指标编码，由系统下发
         */
        private String quotaId;
        /**
         * 指标当前采集值
         */
        private Float value;
        /**
         * 标识本项采集值是否有效 true 表示有效 false 表示无效 无此数据时默认为 true
         *
         */
        private Boolean isValid;
    }
}
