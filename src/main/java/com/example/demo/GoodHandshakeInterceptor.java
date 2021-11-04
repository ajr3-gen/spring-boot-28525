package com.example.demo;

import org.eclipse.jetty.websocket.server.JettyWebSocketServerContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpAsyncRequestControl;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletContext;

@Component
public class GoodHandshakeInterceptor extends MyHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(GoodHandshakeInterceptor.class);
    
    @Autowired
    public GoodHandshakeInterceptor(AuthorizationChecker authCheck) {
        super();
        this.authCheck = authCheck;
    }
    
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
        Map<String, Object> attributes) throws Exception {
        
        // Put request in async mode
        ServerHttpAsyncRequestControl requestControl = request.getAsyncRequestControl(response);
        requestControl.start();

        // You should see that the context is still good when we perform the handshake.
        ServletContext sc2 = ((ServletServerHttpRequest) request).getServletRequest().getServletContext();
        if (Objects.isNull(sc2)) {
            logger.warn("Warning: servletContext is null when performing handshake");
        } else {
            logger.info("performing handshake -- servletContext attribute {} = {}",
                                    JettyWebSocketServerContainer.JETTY_WEBSOCKET_CONTAINER_ATTRIBUTE,
                                    sc2.getAttribute(JettyWebSocketServerContainer.JETTY_WEBSOCKET_CONTAINER_ATTRIBUTE));
        }

        // Perform handshake and complete request
        handshakeHandler.doHandshake(request, response, wsHandler, attributes);
        requestControl.complete();

        return false;
    }

}
