package com.yxy.nova.web;

import com.yxy.nova.bean.UserLoginDTO;
import com.yxy.nova.bean.WebResponse;
import com.yxy.nova.web.util.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: yuxiaoyu
 * @date: 2019-05-16 下午5:45
 */

@Controller
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @PostMapping("/novaWeb/login")
    @ResponseBody
    public WebResponse login(@RequestBody UserLoginDTO loginRequest) throws Exception{
        UsernamePasswordToken token = new UsernamePasswordToken(loginRequest.getLoginId(), loginRequest.getPassword());
        Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(token);
            // 登陆时清除缓存，保证每次都加载最新的Principal
            SecurityUtil.removeCache(loginRequest.getLoginId());
            return WebResponse.success();
        } catch (Exception e) {
            logger.error("", e);
            return WebResponse.fail("用户名或密码错误");
        }
    }

}
