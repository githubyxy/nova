package com.yxy.nova.bean;

/**
 * Created with IntelliJ IDEA.
 *
 * @description: 合作方类型
 * @author: yuxiaoyu
 * @date: 2019-04-24 下午9:48
 */
public enum UserStatusEnum {

    // 正常
    NORMAL("正常"),

    // 禁用
    DISABLED("禁用"),

    //用户删除
    DELETE("删除"),
    ;

    private String desc;

    private UserStatusEnum(String desc) {
        this.desc = desc;
    }

    public String desc() {
        return desc;
    }
}
