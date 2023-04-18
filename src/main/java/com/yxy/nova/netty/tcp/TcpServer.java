package com.yxy.nova.netty.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.Map;

@Slf4j
public class TcpServer {
    private int port;
    private Map<String, TcpHandler> handlers;

    private Channel channel;


    @PostConstruct
    public void startTcpServer(){
        new Thread(() -> {
            EventLoopGroup group = new NioEventLoopGroup(1);
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(group, workerGroup)  //配置一个线程组
                        .channel(NioServerSocketChannel.class)  //设置channel类型为UPD
                        .option(ChannelOption.SO_BROADCAST, true)   //支持广播
                        .option(ChannelOption.SO_RCVBUF, 2048 * 1024)// 设置channel读缓冲区大小
                        .option(ChannelOption.SO_SNDBUF, 2048 * 1024)// 设置channel写缓冲区大小
                        .childHandler(new ChannelInitializer<Channel>() {
                            @Override
                            protected void initChannel(Channel ch) throws Exception {       //装配handler流水线
                                ChannelPipeline pipeline = ch.pipeline();    //Handler将按pipeline中添加的顺序执行
                                pipeline.addLast(new TcpServerHandler(handlers));   //自定义的处理器
                            }
                        });
                //绑定端口（默认是异步的，可以加ChannelFuture的监听事件），sync()同步阻塞等待连接成功；客户端使用.connect(host, port)连接
                ChannelFuture channelFuture = bootstrap.bind(port).sync();
                log.info("tcp服务器启动，端口为{}", port);
//                channel = channelFuture.channel();
                //sync()同步阻塞等待channel关闭
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally{
                //关闭资源
                group.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }).start();

        new Thread(() -> {
            EventLoopGroup group = new NioEventLoopGroup();

            try{
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<Channel>() {
                            @Override
                            protected void initChannel(Channel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                pipeline.addLast(new TcpClientHandler());
                            }
                        });

                ChannelFuture channelFuture = bootstrap.connect("localhost", port).sync();
                channel = channelFuture.channel();
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                group.shutdownGracefully();
            }
        }).start();

    }


    public void singleCast(String host, String pushMsg){
        ByteBuf byteBuf1 = new UnpooledByteBufAllocator(false).buffer();
        byteBuf1.writeCharSequence(pushMsg, CharsetUtil.UTF_8);
        this.channel.writeAndFlush(byteBuf1).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {

            }
        });
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Map<String, TcpHandler> getHandlers() {
        return handlers;
    }

    public void setHandlers(Map<String, TcpHandler> handlers) {
        this.handlers = handlers;
    }
}
