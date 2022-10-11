package com.yxy.nova.netty.udp;

import com.alibaba.fastjson.JSONObject;
import com.yxy.nova.nio.UDPMessage;

public class MyTestUdpHandler implements UdpHandler{
    @Override
    public void execute(UDPMessage udpMessage) {
        System.out.println("MyTestUdpHandler udpMessage：" + JSONObject.toJSONString(udpMessage));
    }
}
