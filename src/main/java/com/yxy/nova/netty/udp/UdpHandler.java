package com.yxy.nova.netty.udp;

import com.yxy.nova.mwh.retry.api.HandlerStatus;
import com.yxy.nova.mwh.retry.api.RetryTaskExecutionContext;
import com.yxy.nova.nio.UDPMessage;

public interface UdpHandler {

    public void execute(UDPMessage udpMessage);

}
