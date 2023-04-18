package com.yxy.nova.netty.tcp;

import com.yxy.nova.mwh.utils.UUIDGenerator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class TcpServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private Map<String, TcpHandler> handlers;
    //创建可缓存线程池
    ExecutorService executorService = Executors.newCachedThreadPool();

    public TcpServerHandler(Map<String, TcpHandler> handlers){
        this.handlers = handlers;
    }

    //监听channel的消息，注意此时的handler为单线程处理，可以把请求加到线程池中提升效率
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] buffer = new byte[msg.readableBytes()];

        msg.readBytes(buffer);

        //将buffer转成字符串
        String message = new String(buffer, Charset.forName("utf-8"));

        System.out.println("服务器接收到数据:"+message);

        //服务器回送数据给客户端,回送一个随机id
        ctx.writeAndFlush(Unpooled.copiedBuffer(UUIDGenerator.generate()+"\n", Charset.forName("utf-8")));
    }

}
