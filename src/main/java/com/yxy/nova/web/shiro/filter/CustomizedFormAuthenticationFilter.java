package com.yxy.nova.web.shiro.filter;

import com.yxy.nova.bean.WebResponse;
import com.yxy.nova.web.util.WebUtil;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: yuxiaoyu
 * @date: 2019-12-05 下午7:33
 */
public class CustomizedFormAuthenticationFilter extends FormAuthenticationFilter {
    @Override
    protected void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        if (WebUtil.isAjaxRequest(httpServletRequest.getServletPath())) {
            // 输出JSON
            WebUtil.writeJson(WebResponse.fail("未登录"), httpResponse);
        } else {
            super.saveRequestAndRedirectToLogin(request, response);
        }
    }
}
