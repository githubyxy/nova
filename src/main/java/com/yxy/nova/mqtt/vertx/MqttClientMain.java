package com.yxy.nova.mqtt.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MqttClientMain extends AbstractVerticle {

    public static void main(String[] args) throws Exception {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(MqttClientApp.class.getName());
    }
}
