package com.yxy.nova.nio;

import java.util.concurrent.*;

public class UDPServer {

    private final static ThreadPoolExecutor POOL_EXECUTOR = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2,
            1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(5));


    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        UdpChannelServer udpChannelServer = new UdpChannelServer("127.0.0.1", 8888);

        Future<String> submit = POOL_EXECUTOR.submit(udpChannelServer);
        String result = submit.get();
        System.out.println(result);
    }
}
