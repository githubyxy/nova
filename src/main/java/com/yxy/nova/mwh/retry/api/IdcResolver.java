package com.yxy.nova.mwh.retry.api;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @description: 该接口被RetryTaskScheduler用来选择处理哪些机房的数据
 * @author: shui.ren
 * @date: 2019-08-12 上午10:42
 */
public interface IdcResolver {

    /**
     * 未指定机房标识
     */
    String IDC_MISSING = "NULL";

    /**
     * 返回机房标识列表。"NULL"代表调度器需要处理未指定机房标识的数据。
     * @return
     */
    List<String> resolve();
}
