package com.yxy.nova.web.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: yuxiaoyu
 * @date: 2019-04-23 下午6:51
 */
public class WebUtil {

    private static final String HTTP_HEADER_X_REQUESTED_WITH = "x-requested-with";

    private static final String HTTP_HEADER_X_REQUESTED_WITH_XMLHTTPREQUEST = "XMLHttpRequest";

    private static final String[] STATIC_RESOURCE_REQUEST_PREFIX_ARRAY = {"/statics/","/code/", "/css/", "/datas/", "/images/", "/img/", "/js/", "/layui/", "/plugins.layui/", "/svg/", "plugins"};

    private static final String UNKNOWN = "unknown";

    private static final String HERTZ_X_REQUESTED = "/novaWeb";

    /**
     * openapi请求前缀
     */
    private static final String HERTZ_OPENAPI_REQUEST_URL_PREFIX = "/openapi";

    /**
     * 获取完整的包含查询参数的url
     * @param request
     * @return
     */
    public static String getFullURL(HttpServletRequest request) {
        StringBuffer fullURL = request.getRequestURL();
        if (request.getQueryString() != null) {
            fullURL.append("?").append(request.getQueryString());
        }
        return fullURL.toString();
    }

    /**
     * 是否是Ajax请求
     * @param requestUri
     * @return
     */
    public static boolean isAjaxRequest(String requestUri) {
        if (StringUtils.indexOf(requestUri, HERTZ_X_REQUESTED) == 0){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否是openapi请求
     * @param requestUri
     * @return
     */
    public static boolean isOpenApiRequest(String requestUri) {
        if (StringUtils.contains(requestUri, HERTZ_OPENAPI_REQUEST_URL_PREFIX)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 输出JSON
     * @param result
     * @param response
     */
    public static void writeJson(Object result, HttpServletResponse response) {
        doWrite(result,response, MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    private static void doWrite(Object result, HttpServletResponse response, String mediaType) {
        PrintWriter out = null;
        try {
            //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
            response.setCharacterEncoding("utf-8");
            //这句话的意思，是让浏览器用utf8来解析返回的数据
            response.setContentType(mediaType);
            //这句话一定要在setCharacterEncoding之后
            out = response.getWriter();
            out.print(JSON.toJSONString(result));
        } catch (IOException e) {

        } finally {
            if (out != null) {
                out.close();
            }
        }
    }


    /**
     * 该请求是否是静态资源请求
     * @param request
     * @return
     */
    public static boolean isStaticResourceRequest(HttpServletRequest request) {

        String requestURI = request.getRequestURI();

        for (String prefix : STATIC_RESOURCE_REQUEST_PREFIX_ARRAY) {
            if (requestURI.startsWith(prefix)) {
                return true;
            }
        }

        return false;

    }

    /**
     * 获取客户端的ip
     * @param request
     * @return
     */
    public static String getRemoteAddr(HttpServletRequest request) {

        String ip = StringUtils.trimToEmpty(request.getHeader("x-real-ip"));

        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = StringUtils.trimToEmpty(request.getHeader("x-forwarded-for"));
            ip = StringUtils.trimToEmpty(ip.split(",")[0]);
        }

        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = StringUtils.trimToEmpty(request.getHeader("Proxy-Client-IP"));
        }

        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = StringUtils.trimToEmpty(request.getHeader("WL-Proxy-Client-IP"));
        }

        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return StringUtils.trimToEmpty(ip);
    }
}
