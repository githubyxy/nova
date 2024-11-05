package com.yxy.nova.service.impl;

import org.springframework.stereotype.Service;

/**
 * @author yuxiaoyu
 * @date 2021/3/5 下午6:07
 * @Description
 */
@Service
public class MyPowerServiceImpl implements MyPowerService {


    @Override
    public void insert(String content) {
        return;
    }

    @Override
    public String getAllPower() {
        return "getAllPower";
    }

    @Override
    public String fuzzyQuery(String content) {
        return content;
    }
}
