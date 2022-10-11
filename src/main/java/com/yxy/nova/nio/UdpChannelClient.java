package com.yxy.nova.nio;

import lombok.SneakyThrows;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

public class UdpChannelClient {

    private String ip;
    private int port;

    public UdpChannelClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @SneakyThrows
    public void send(String content) {
        DatagramChannel channel = DatagramChannel.open();
        channel.configureBlocking(false);

        Selector selector = Selector.open();
        SelectionKey selectionKey1 = channel.register(selector, SelectionKey.
                OP_WRITE);
        int keyCount = selector.select();
        Set<SelectionKey> selectedKeysSet = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectedKeysSet.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            if (key.isWritable()) {
                byte[] buff = content.getBytes();

                // 设置发送用的DatagramPacket里的字节数据
                ByteBuffer buffer = ByteBuffer.wrap(buff);
                // 如果在两台物理计算机中进行实验，则要把localhost改成客户端的IP地址
                channel.send(buffer, new InetSocketAddress(ip, port));


                channel.close();
            }
        }
        System.out.println("client end !");
    }
}
