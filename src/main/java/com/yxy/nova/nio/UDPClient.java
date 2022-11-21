package com.yxy.nova.nio;

import com.alibaba.fastjson.JSONObject;

import java.nio.ByteBuffer;

public class UDPClient {
    private static   ByteBuffer buffer = null;

    public static void main(String[] args) throws Exception {
        UdpChannelClient client = new UdpChannelClient("127.0.0.1", 8888);
        UDPMessage udpMessage = new UDPMessage();
        udpMessage.setKey("key2");
        udpMessage.setContent("于晓宇2");
        client.send(JSONObject.toJSONString(udpMessage));
    }
}
