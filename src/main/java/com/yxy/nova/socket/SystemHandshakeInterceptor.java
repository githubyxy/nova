package com.yxy.nova.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * @author yuxiaoyu
 * @date 2020/9/8 下午2:34
 * @Description
 */
@Slf4j
@Component
public class SystemHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("beforeHandshake {}", request.getPrincipal().getName());
        //解决The extension [x-webkit-deflate-frame] is not supported问题
        if(request.getHeaders().containsKey("Sec-WebSocket-Extensions")){
            request.getHeaders().set("Sec-WebSocket-Extensions", "permessage-deflate");
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        log.info("afterHandshake {}", request.getPrincipal().getName());
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
