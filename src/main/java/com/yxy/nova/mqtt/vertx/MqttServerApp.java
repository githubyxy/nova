package com.yxy.nova.mqtt.vertx;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttEndpoint;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class MqttServerApp extends AbstractVerticle {
    private Logger log = LoggerFactory.getLogger(getClass());

    private final static String CLIENT_ID = "clientHello";

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
//        ConfigProperties properties = config().mapTo(ConfigProperties.class);
//        int port = properties.getServer().getPort();
//        log.info("===>json: {}, port: {}", properties, port);

        MqttServer mqttServer = MqttServer.create(vertx, create());
        mqttServer.endpointHandler(endpoint -> {
                    // shows main connect info
                    System.out.println("MQTT client ["+endpoint.clientIdentifier()+"] request to connect, clean session = " + endpoint.isCleanSession());
                    if (endpoint.auth() != null) {
                        System.out.println("[username = " +endpoint.auth().getUsername()+", password = " + endpoint.auth().getPassword());
                    }

//                    log.info("[properties = {}]", JSON.toJSONString(endpoint.connectProperties()));
                    if (endpoint.will() != null) {
                        System.out.println("[will topic:" + endpoint.will().getWillTopic() + "msg:" + endpoint.will().getWillMessageBytes() + "QoS:" + endpoint.will().getWillQos() + "isRetain:" + endpoint.will().isWillRetain());
                    }

                    System.out.println("[keep alive timeout = {}]" + endpoint.keepAliveTimeSeconds());
                    // accept connection from the remote client
                    endpoint.accept(true);
                    receiver(endpoint);
                    endpoint.disconnectMessageHandler(disconnectMessage -> System.out.println("Received disconnect from client, reason code = " + disconnectMessage.code()));
                })
                .exceptionHandler(t -> System.out.println("MQTT exception fail: " + t))
                .listen(ar -> {
                    if (ar.succeeded()) {
                        System.out.println("MQTT server is listening on port: " + ar.result().actualPort());
                    } else {
                        System.out.println("Fail on starting the server: "+ ar.cause());
                    }
                });

    }

    private void receiver(MqttEndpoint endpoint) {
        endpoint.publishHandler(p -> {
                    System.out.println("Server received message [{" + p.payload().toString(Charset.defaultCharset()) + "}] with QoS [{ " + p.qosLevel() + "}]");
                    if (p.qosLevel() == MqttQoS.AT_LEAST_ONCE) {
                        endpoint.publishAcknowledge(p.messageId());
                    } else if (p.qosLevel() == MqttQoS.EXACTLY_ONCE) {
                        endpoint.publishReceived(p.messageId());
                    }
                    send(endpoint);
                })
                .publishReleaseHandler(endpoint::publishComplete);
    }

    private void send(MqttEndpoint endpoint) {
        Buffer payload = Buffer.buffer("server: hello world.");
        endpoint.publish(MqttClientApp.MQTT_TOPIC, payload, MqttQoS.AT_MOST_ONCE, false, false, s -> {
            if (s.succeeded()) {
                System.out.println("===>Server publish success: " + s.result());
            } else {
                System.out.println("===>Server publish fail: " + s.cause());
            }
        });
    }

    private MqttServerOptions create() {
        MqttServerOptions options = new MqttServerOptions();
        options.setPort(18003);
        options.setHost("127.0.0.1");
        return options;
    }
    private MqttServerOptions create(ConfigProperties configProperties) {
        MqttServerOptions options = new MqttServerOptions();
        options.setPort(configProperties.getServer().getPort());
        options.setHost(configProperties.getServer().getHost());
        return options;
    }

}
