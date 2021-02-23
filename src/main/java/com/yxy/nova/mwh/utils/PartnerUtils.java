package com.yxy.nova.mwh.utils;


import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 类PartnerUtils
 *
 * @author toruneko 15/8/26 10:50
 */
public final class PartnerUtils {

    /**
     * 判断是否为同盾合作方
     */
    public static boolean isFraudmetrix(String partnerCode) {
        return "fraudmetrix".equalsIgnoreCase(partnerCode);
    }
    
    /**
     * 判断应用列表是否选择“全部”
     */
    public static boolean isAllApp(String appName){
        return "all".equalsIgnoreCase(appName);
    }

    /**
     * 获取用于请求的partnerList
     * @param partnerCode 用于认证的partnerCode
     * @param partners 用于检索的partners
     * @return
     */
    public static List<String> getPartnerList(String partnerCode, String partners){
        if (isFraudmetrix(partnerCode)) {
            if (StringUtils.isBlank(partners) || "all".equalsIgnoreCase(partners)) {
                return Collections.emptyList();
            } else {
                return Arrays.asList(partners.split(","));
            }
        } else {
            return Arrays.asList(partnerCode);
        }
    }
}
