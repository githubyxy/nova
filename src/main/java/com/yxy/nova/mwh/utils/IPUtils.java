package com.yxy.nova.mwh.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.util.IPAddressUtil;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by liulongli on 2017/7/26.
 */
public class IPUtils {
    private static final Logger logger = LoggerFactory.getLogger(IPUtils.class);

    /**
     * 获取本机IP
     *
     * @return
     */
    public static String getLocalHostAddress() {
        Enumeration<NetworkInterface> netInterfaces = null;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                // 过滤回路ip，如127.0.0.1
                if (ni.isLoopback()) {
                    continue;
                }
                // 过滤网卡状态为down的信息
                if (!ni.isUp()) {
                    continue;
                }
                // 过滤虚拟IP
                if (ni.isVirtual()) {
                    continue;
                }
                while (ips.hasMoreElements()) {
                    InetAddress inetAddress = ips.nextElement();
                    String hostAddress = inetAddress.getHostAddress();
                    if (!IPAddressUtil.isIPv6LiteralAddress(hostAddress)) {
                        return hostAddress;
                    }
                }
            }
        } catch (SocketException e) {
            logger.error("get local host address error",e);
        }

        return null;
    }
}
