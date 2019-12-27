package com.yxy.nova.web.shiro.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @description: 拦截openapi请求
 * @author: yuxiaoyu
 * @date: 2019-04-22 下午8:58
 */
public class OpenApiFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String cloudId = req.getHeader("x-access-key");
        String secretKey = req.getHeader("x-secret-key");

        // TODO: 校验是否匹配

        // ===调用真正的业务逻辑===
        filterChain.doFilter(servletRequest, servletResponse);
        // ===真正的业务逻辑调用结束===
    }


    @Override
    public void destroy() {

    }
}
