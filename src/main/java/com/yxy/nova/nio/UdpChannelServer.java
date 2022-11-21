package com.yxy.nova.nio;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;

public class UdpChannelServer implements Callable<String> {

    private String ip;
    private int port;

    public UdpChannelServer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String call() throws Exception {
        DatagramChannel channel = DatagramChannel.open();
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress(ip, port));
        Selector selector = Selector.open();
        SelectionKey selectionKey = channel.register(selector, SelectionKey.OP_READ);
        boolean isRun = true;
        String str = null;
        while (isRun) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isReadable()) {
                    channel = (DatagramChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    channel.receive(buffer);
                    str = new String(buffer.array(), 0, buffer.position());
                }
                iterator.remove();
            }
            UDPMessage message = JSONObject.parseObject(StringUtils.trimToEmpty(str), new TypeReference<UDPMessage>() {
            });
            System.out.println(JSON.toJSONString(message));
            // 根据key 找到自己的handler去处理业务逻辑

        }
        channel.close();
        return str;
    }
}
