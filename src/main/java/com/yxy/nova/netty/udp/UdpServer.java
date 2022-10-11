package com.yxy.nova.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.Map;

@Slf4j
public class UdpServer {
    private int port;
    private Map<String, UdpHandler> handlers;

    private Channel channel;


    @PostConstruct
    public void startUdpServer(){
        new Thread(() -> {
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group)  //配置一个线程组
                        .channel(NioDatagramChannel.class)  //设置channel类型为UPD
                        .option(ChannelOption.SO_BROADCAST, true)   //支持广播
                        .option(ChannelOption.SO_RCVBUF, 2048 * 1024)// 设置channel读缓冲区大小
                        .option(ChannelOption.SO_SNDBUF, 2048 * 1024)// 设置channel写缓冲区大小
                        .handler(new ChannelInitializer<Channel>() {
                            @Override
                            protected void initChannel(Channel ch) throws Exception {       //装配handler流水线
                                ChannelPipeline pipeline = ch.pipeline();    //Handler将按pipeline中添加的顺序执行
                                pipeline.addLast(new UdpServerHandler(handlers));   //自定义的处理器
                            }
                        });
                //绑定端口（默认是异步的，可以加ChannelFuture的监听事件），sync()同步阻塞等待连接成功；客户端使用.connect(host, port)连接
                ChannelFuture channelFuture = bootstrap.bind(port).sync();
                log.info("udp服务器启动，端口为{}", port);
                channel = channelFuture.channel();
                //sync()同步阻塞等待channel关闭
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally{
                //关闭资源
                group.shutdownGracefully();
            }
        }).start();
    }


    public void singleCast(String host, String pushMsg){
        InetSocketAddress remoteAddress = new InetSocketAddress(host, port);    //远程地址
        ByteBuf byteBuf1 = new UnpooledByteBufAllocator(false).buffer();
        byteBuf1.writeCharSequence(pushMsg, CharsetUtil.UTF_8);
        DatagramPacket packet = new DatagramPacket(byteBuf1, remoteAddress);
        this.channel.writeAndFlush(packet);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Map<String, UdpHandler> getHandlers() {
        return handlers;
    }

    public void setHandlers(Map<String, UdpHandler> handlers) {
        this.handlers = handlers;
    }
}
