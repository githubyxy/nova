package com.yxy.nova.service.wechat;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yuxiaoyu
 * @date 2021/1/22 上午11:38
 * @Description
 */
public interface WechatService {

    String weixinPost(HttpServletRequest request);

    String getAccessToken();
}
