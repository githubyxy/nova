package test;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yuxiaoyu
 * @Description: API批量黑名单上传请求参数
 * @date 2019-06-13 10:10
 */
@Data
public class BlacklistUploadParamDTO implements Serializable {

    @JSONField(name="group_uuid")
    private String groupUuid;
    @JSONField(name="partner_code")
    private String partnerCode;
    @JSONField(name="mobile_list")
    private List<String> mobileList;
    @JSONField(name="expire_date")
    private String expireDate;

    /**
     * 单个号码单个过期时间
     */
    @JSONField(name="blacklist_info")
    private List<BlacklistInfo> blacklistInfo;

    @Data
    public static class BlacklistInfo {
        @JSONField(name="mobile")
        private String mobile;
        @JSONField(name="expire_date")
        private String expireDate;
    }
}
