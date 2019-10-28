package com.yxy.nova.web;

import com.yxy.nova.bean.*;
import com.yxy.nova.dal.mysql.dataobject.UserDO;
import com.yxy.nova.service.UserService;
import com.yxy.nova.util.MapCache;
import com.yxy.nova.util.Md5Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author yuxiaoyu
 * @Description:
 * @date 2019-07-05 15:39
 */
@Controller()
@RequestMapping(value = "auth")
public class AuthController {

    @Autowired
    private UserService userService;

    private final MapCache mapCache = MapCache.single();


    @GetMapping(value = "/gotoLogin")
    public String gotoLogin(){
        return "login";
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public WebResponse login(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request, HttpServletResponse response){

        try {
            UserDO userDO = userService.login(userLoginDTO);
            request.setAttribute("login_user", userDO);

            String token = Md5Encrypt.encrypt(UUID.randomUUID().toString());
            boolean isSSL = request.getProtocol().startsWith("https");
            Cookie cookie = new Cookie(Constants.login_cookie_name, token);
            cookie.setHttpOnly(true);
            cookie.setSecure(isSSL);
            cookie.setMaxAge(30 * 60);
            cookie.setPath("/");
            response.addCookie(cookie);

            mapCache.set(token, userDO, 30 * 60);

            return WebResponse.success();
        } catch (BizException e) {

            return WebResponse.fail(e.getReasonDesc(), e.getMessage());
        } catch (Exception e) {
            return WebResponse.fail(ReasonCode.SYS_ERROR.getDesc(), "系统异常");
        }
    }


}
