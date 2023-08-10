package com.yxy.nova.netty.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TcpClient {
    private String host;
    private int port;
    private EventLoopGroup group;
    private Bootstrap bootstrap;
    private Channel channel;

    @PostConstruct
    public void start() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
                        ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
                        ch.pipeline().addLast(new TcpClientHandler());
                    }
                });

        doConnect();
    }

    public void stop() {
        if (channel != null) {
            channel.close();
        }
        group.shutdownGracefully();
    }

    private void doConnect() {
        if (channel != null && channel.isActive()) {
            return;
        }

        ChannelFuture future = bootstrap.connect(host, port);
        future.addListener((ChannelFuture cf) -> {
            if (cf.isSuccess()) {
                channel = cf.channel();
                log.info("connect to server success");
            } else {
                log.error("Failed to connect to server. Retrying in 5 seconds...");
                cf.channel().eventLoop().schedule(this::doConnect, 5, TimeUnit.SECONDS);
            }
        });
    }

    public void sendRequest(String request) {
        if (channel != null && channel.isActive()) {
            ByteBuf byteBuf1 = new UnpooledByteBufAllocator(false).buffer();
            byteBuf1.writeCharSequence(request, CharsetUtil.UTF_8);
            channel.writeAndFlush(byteBuf1);
        } else {
            log.error("Connection is not active. Unable to send request.");
        }
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private class TcpClientHandler extends SimpleChannelInboundHandler<String> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            System.out.println("Received response: " + msg);
            // Handle response from the server
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            System.err.println("Connection lost. Retrying in 5 seconds...");
            ctx.channel().eventLoop().schedule(TcpClient.this::doConnect, 5, TimeUnit.SECONDS);
            super.channelInactive(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.err.println("exceptionCaught ...");
            cause.printStackTrace();
            ctx.close();
        }
    }

}
