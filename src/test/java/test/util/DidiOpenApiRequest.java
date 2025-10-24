package test.util;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: yxy
 * @description: 滴滴openapi请求的对象
 */
@Data
public class DidiOpenApiRequest implements Serializable {
    private static final long serialVersionUID = -2584470985635978248L;

    // 滴滴分配的供应商编码，回调滴滴时才需要设置值
    private String supplierCode;

    private String version;

    private String traceId;

    private long timestamp;

    private String sign;

    // aes加密的密文
    private String body;
    // aes将body解密后的明文
    private JSONObject jsonBody;


    /**
     * 请求滴滴时，封装公共参数
     */
    public static DidiOpenApiRequest geneDidiOpenApiRequest(String supplierCode, String secretKey, String accessKey, JSONObject jsonBody) {
        DidiOpenApiRequest request = new DidiOpenApiRequest();
        request.setSupplierCode(supplierCode);
        request.setVersion("1");
        request.setTraceId(com.yxy.nova.mwh.utils.UUIDGenerator.generate());
        long timestamp = System.currentTimeMillis() / 1000;
        request.setTimestamp(timestamp);
        request.setBody(DidiSignUtil.bodyEncryptByAccessKey(jsonBody.toJSONString(), accessKey, timestamp));
        request.setSign(DidiSignUtil.generateOuterSign(request.getBody(), supplierCode, secretKey, timestamp));
        return  request;
    }
}
