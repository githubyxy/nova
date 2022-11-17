package com.yxy.nova.mqtt.vertx;

import lombok.Data;

/**
 * config properties
 *
 * @author yxy
 * @date 2022/1/21 20:05
 */
@Data
public class ConfigProperties {
    private ServerProperties server;

    @Data
    public static class ServerProperties {
        private int port;
        private String host;
    }
}
