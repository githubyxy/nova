package com.yxy.nova.netty.udp;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yxy.nova.nio.UDPMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private Map<String, UdpHandler> handlers;
    //创建可缓存线程池
    ExecutorService executorService = Executors.newCachedThreadPool();

    public UdpServerHandler(Map<String, UdpHandler> handlers){
        this.handlers = handlers;
    }

    //监听channel的消息，注意此时的handler为单线程处理，可以把请求加到线程池中提升效率
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        DatagramPacket p = packet.copy();
        //将请求放在线程池处理
        executorService.execute(new UdpHandlerRunnable(p));
    }

    class UdpHandlerRunnable implements Runnable{
        DatagramPacket packet;
        UdpHandlerRunnable(DatagramPacket packet){
            this.packet = packet;
        }
        public void run(){
            ByteBuf byteBuf = packet.content();
            byteBuf.retain();       // byteBuf引用计数加1，避免报引用为0异常
            String response = new String(ByteBufUtil.getBytes(byteBuf));
            log.info("收到来自 "+ packet.sender() + " 的请求, response {}", response);
            // 业务逻辑
            UDPMessage message = JSONObject.parseObject(StringUtils.trimToEmpty(response), new TypeReference<UDPMessage>() {
            });
            handlers.get(message.getKey()).execute(message);
            byteBuf.release();  //注意释放byteBuf和packet
            packet.release();
        }
    }

}
