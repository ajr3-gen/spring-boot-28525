package com.example.demo;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.util.Map;

@Component
public abstract class MyHandshakeInterceptor implements HandshakeInterceptor {

    protected HandshakeHandler handshakeHandler;
    protected AuthorizationChecker authCheck;
    
    public MyHandshakeInterceptor() {
        handshakeHandler = new DefaultHandshakeHandler();
    }
    
    @Override
    public abstract boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
        Map<String, Object> attributes) throws Exception;

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Exception exception) {
        // Nothing to do
    }

    public HandshakeHandler getHandshakeHandler() {
        return handshakeHandler;
    }

}
