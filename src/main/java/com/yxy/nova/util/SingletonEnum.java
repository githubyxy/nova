package com.yxy.nova.util;

import com.yxy.nova.service.impl.MyOpenAiService;
import com.yxy.nova.service.impl.MyOpenAiServiceImpl;
import com.yxy.nova.service.impl.MyPowerService;
import com.yxy.nova.service.impl.MyPowerServiceImpl;

public enum SingletonEnum {
    INSTANCE;


    private  MyOpenAiService myOpenAiService;
    private  MyPowerService myPowerService;

    SingletonEnum() {
        myOpenAiService = new MyOpenAiServiceImpl();
        myPowerService = new MyPowerServiceImpl();
    }

    public MyOpenAiService getMyOpenAiService() {
        return myOpenAiService;
    }

    public MyPowerService getMyPowerService() {
        return myPowerService;
    }

//    public static void main(String[] args) {
//        System.out.println(SingletonEnum.INSTANCE.getMyOpenAiService() == SingletonEnum.INSTANCE.getMyOpenAiService());
//    }

}
