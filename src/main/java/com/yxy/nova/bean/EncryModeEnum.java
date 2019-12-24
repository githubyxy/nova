package com.yxy.nova.bean;

/**
 * @Description: 加密方式
 * @author yuxiaoyu
 * @date 2019-06-12 15:51
 */
public enum EncryModeEnum {
    MD5("md5"),
    SHA256("sha256");
    private String desc;

    EncryModeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
