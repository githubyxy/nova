package com.yxy.nova.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yuxiaoyu
 * @date 2019/12/24 下午3:25
 * @Description
 */
@Data
public class EncryptRequest implements Serializable {
    private static final long serialVersionUID = 6893472720934192095L;
    private String plaintext;
    private String encryModeEnum;
}
