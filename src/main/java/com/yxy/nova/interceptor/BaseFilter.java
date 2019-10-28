package com.yxy.nova.interceptor;

import com.yxy.nova.bean.Constants;
import com.yxy.nova.dal.mysql.dataobject.UserDO;
import com.yxy.nova.util.MapCache;
import com.yxy.nova.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BaseFilter implements Filter {

    private static final Logger LOGGE = LoggerFactory.getLogger(BaseFilter.class);

    private MapCache cache = MapCache.single();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request  = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        LOGGE.info("用户访问地址: {}, 来路地址: {}", request.getRequestURL(), WebUtil.getRemoteAddr(request));


        String token = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equals(Constants.login_cookie_name)) {
                    token = cookie.getValue();
                }
            }
        }

        UserDO userDO = cache.get(token);
        String requestURI = request.getRequestURI();

        if (requestURI.contains("/auth")) {
            if (userDO != null) {
                response.sendRedirect(request.getContextPath() + "/biz/index");
                return;
            }
        } else {
            if (userDO == null) {
                response.sendRedirect(request.getContextPath() + "/auth/gotoLogin");
                return;
            }
        }

        cache.set(token, userDO,30*60);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
