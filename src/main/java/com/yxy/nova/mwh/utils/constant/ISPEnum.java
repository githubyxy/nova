package com.yxy.nova.mwh.utils.constant;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Created by chenchanglong on 2019/7/17.
 */
public enum ISPEnum {

    CMCC("中国移动", "移动"),
    CUCC("中国联通", "联通"),
    CTCC( "中国电信", "电信");

    //编码
    private String desc;
    //简称
    private String abbr;

    ISPEnum(String desc, String abbr) {
        this.desc = desc;
        this.abbr = abbr;
    }


    public String getDesc() {
        return desc;
    }

    public String getAbbr() {
        return abbr;
    }

    public static ISPEnum valueOfByName(String ispName) {
        if(StringUtils.isBlank(ispName)) {
            return null;
        }
        return Arrays.stream(ISPEnum.values())
                .filter(entry -> StringUtils.contains(ispName, entry.getAbbr()))
                .findFirst().orElse(null);
    }
}
