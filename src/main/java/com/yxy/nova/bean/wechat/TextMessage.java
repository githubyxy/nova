package com.yxy.nova.bean.wechat;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yuxiaoyu
 * @date 2021/1/22 上午11:42
 * @Description
 */
@Data
public class TextMessage implements Serializable {

    private String ToUserName;
    private String FromUserName;
    private String CreateTime;
    private String Content;
    private String MsgType;
    private String MsgId;
}
