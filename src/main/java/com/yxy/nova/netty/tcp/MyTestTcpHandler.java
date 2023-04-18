package com.yxy.nova.netty.tcp;

import com.alibaba.fastjson.JSONObject;
import com.yxy.nova.nio.UDPMessage;

public class MyTestTcpHandler implements TcpHandler {
    @Override
    public void execute(UDPMessage udpMessage) {
        System.out.println("MyTestTcpHandler udpMessage：" + JSONObject.toJSONString(udpMessage));
    }
}
