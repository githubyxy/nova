package com.yxy.nova.netty.tcp;

import com.yxy.nova.nio.UDPMessage;

public interface TcpHandler {

    public void execute(UDPMessage udpMessage);

}
