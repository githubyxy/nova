package com.yxy.nova.service.impl;

/**
 * @author yuxiaoyu
 * @date 2021/3/5 下午6:07
 * @Description
 */
public interface MyPowerService {

    void insert(String content);

    String getAllPower();

    String fuzzyQuery(String content);
}
