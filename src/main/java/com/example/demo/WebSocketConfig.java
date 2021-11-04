package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketController greetingController;
    private final GoodHandshakeInterceptor goodInterceptor;
    private final BadHandshakeInterceptor badInterceptor;
    
    @Autowired
    public WebSocketConfig(WebSocketController greetingController, GoodHandshakeInterceptor goodInterceptor, BadHandshakeInterceptor badInterceptor) {
        this.greetingController= greetingController;
        this.goodInterceptor = goodInterceptor;
        this.badInterceptor = badInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(greetingController, "/websocket/good")
        .addInterceptors(goodInterceptor)
        .setAllowedOrigins("*")
        .setHandshakeHandler(goodInterceptor.getHandshakeHandler());
        
        registry.addHandler(greetingController, "/websocket/bad")
        .addInterceptors(badInterceptor)
        .setAllowedOrigins("*")
        .setHandshakeHandler(badInterceptor.getHandshakeHandler());
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WebSocketController();
    }
    
}