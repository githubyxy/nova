package com.yxy.nova.bean;


import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * web接口响应类封装
 */
@Data
public class WebResponse<T> implements Serializable {

    private static final long serialVersionUID = -3288245732024701355L;

    private Object            data;

    private Boolean           success;

    private String            code;

    private String            message;

    /**
     * 返回单值对象
     *
     * @param t 要返回的业务数据
     */
    public static <T> WebResponse<T> successData(T t, String msg) {
        WebResponse<T> result = successData(t);
        result.setMessage(msg);

        return result;
    }

    /**
     * 分页返回
     * @param t
     * @param total
     * @param currentPage
     * @param pageSize
     * @param <T>
     * @return
     */
    public static<T> WebResponse<T> successPage(T t, int total,int currentPage,int pageSize){
        WebResponse<T> result = success();
        Map<String, Object> data = new HashMap<>(4);
        data.put("total", total);
        data.put("list", t);
        data.put("currentPage", currentPage);
        data.put("pageSize", pageSize);
        result.setData(data);

        return result;
    }

    /**
     * 返回单值对象
     *
     * @param t 要返回的业务数据
     */
    public static <T> WebResponse<T> successData(T t) {
        WebResponse<T> webResponse = success();
        webResponse.setData(t);

        return webResponse;
    }


    public static WebResponse success(){
        WebResponse webResponse = new WebResponse();
        webResponse.setSuccess(true);
        webResponse.setMessage(ReasonCode.SUCCESS.getDesc());

        return webResponse;
    }

    /**
     *  返回列表数据
     *
     * @param t 要返回的业务数据
     */
    public static <T> WebResponse<T> successList(T t, int total) {
        WebResponse<T> result = success();
        Map<String, Object> data = new HashMap<>(2);
        data.put("total", total);
        data.put("list", t);
        result.setData(data);

        return result;
    }

    /**
     * 失败
     * @param msg
     * @return
     */
    public static WebResponse fail(String msg){
        WebResponse webResponse = new WebResponse();
        webResponse.setSuccess(false);
        webResponse.setMessage(msg);

        return webResponse;
    }

    /**
     * 失败
     * @param msg
     * @return
     */
    public static WebResponse fail(String code, String msg){
        WebResponse webResponse = new WebResponse();
        webResponse.setSuccess(false);
        webResponse.setCode(code);
        webResponse.setMessage(msg);

        return webResponse;
    }
}

