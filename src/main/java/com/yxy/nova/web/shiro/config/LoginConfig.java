package com.yxy.nova.web.shiro.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 登录相关的配置
 * @author yuxiaoyu
 */
@Component
@Data
public class LoginConfig {

    @Value("${global.session.timeout}")
    private Long globalSessionTimeout;

}

