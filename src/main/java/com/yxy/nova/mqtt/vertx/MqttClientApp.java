package com.yxy.nova.mqtt.vertx;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import org.apache.commons.lang3.RandomStringUtils;

import java.nio.charset.Charset;

public class MqttClientApp extends AbstractVerticle {
    public static final String MQTT_TOPIC = "hello_topic";

    @Override
    public void start() {
        MqttClient client = MqttClient.create(vertx, create());

        // handler will be called when we have a message in topic we subscribe for
        client.publishHandler(p -> {
            System.out.println("Client received message on ["+p.topicName()+"] payload ["+p.payload().toString(Charset.defaultCharset())+"] with QoS:" + p.qosLevel());
        });

        client.connect(18003, "127.0.0.1", s -> {
            if (s.succeeded()) {
                System.out.println("Client connect success.");
                subscribe(client);
            } else {
                System.out.println("Client connect fail: " + s.cause());
            }
        }).exceptionHandler(event -> {
            System.out.println("client fail: " + event.getCause());
        });
    }

    private void subscribe(MqttClient client) {
        client.subscribe(MQTT_TOPIC, 0, e -> {
            if (e.succeeded()) {
                System.out.println("===>subscribe success: "+ e.result());
                vertx.setPeriodic(10_000, l -> publish(client));
            } else {
                System.out.println("===>subscribe fail: " + e.cause());
            }
        });
    }

    private void publish(MqttClient client) {
        Buffer payload = Buffer.buffer("client: hello world.");
        client.publish(MQTT_TOPIC, payload, MqttQoS.AT_MOST_ONCE, false, false, s -> {
            if (s.succeeded()) {
                System.out.println("===>Client publish success: " + s.result());
            } else {
                System.out.println("===>Client publish fail: " + s.cause());
            }
        });
    }

    private MqttClientOptions create() {
        MqttClientOptions options = new MqttClientOptions();
        options.setClientId("ClientId_" + RandomStringUtils.randomAlphanumeric(6));
        options.setMaxMessageSize(100_000_000);
        options.setKeepAliveInterval(2);
        return options;
    }

}
