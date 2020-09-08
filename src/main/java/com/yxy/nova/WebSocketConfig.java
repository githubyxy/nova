package com.yxy.nova;

import com.yxy.nova.socket.PrincipalHandshakeHandler;
import com.yxy.nova.socket.SystemHandshakeInterceptor;
import com.yxy.nova.socket.SystemWebSocketHandlerDecoratorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * @author yuxiaoyu
 * @date 2020/9/8 下午2:36
 * @Description
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private SystemHandshakeInterceptor systemHandshakeInterceptor;
    @Autowired
    private SystemWebSocketHandlerDecoratorFactory systemWebSocketHandlerDecoratorFactory;
    @Autowired
    private PrincipalHandshakeHandler principalHandshakeHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /**
         * myUrl表示 你前端到时要对应url映射
         */
        registry.addEndpoint("/myUrl")
                .setAllowedOrigins("*")
                .setHandshakeHandler(principalHandshakeHandler)
                .addInterceptors(systemHandshakeInterceptor)
                .withSockJS();
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        /**
         * queue 点对点
         * topic 广播
         * user 点对点前缀
         */
        registry.enableSimpleBroker("/queue", "/topic");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(8192)
                .setSendBufferSizeLimit(8192)
                .addDecoratorFactory(systemWebSocketHandlerDecoratorFactory);
    }
}
