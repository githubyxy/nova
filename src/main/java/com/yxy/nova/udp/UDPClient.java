package com.yxy.nova.udp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

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
