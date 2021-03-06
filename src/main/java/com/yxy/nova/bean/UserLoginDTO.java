package com.yxy.nova.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yuxiaoyu
 * @Description:
 * @date 2019-10-28 13:58
 */
@Data
public class UserLoginDTO implements Serializable {
    /**
     * 登录账号
     */
    private String loginId;

    /**
     * 密码
     */
    private String password;
}
