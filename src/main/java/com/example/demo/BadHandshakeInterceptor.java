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
public class BadHandshakeInterceptor extends MyHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(BadHandshakeInterceptor.class);
    
    @Autowired
    public BadHandshakeInterceptor(AuthorizationChecker authCheck) {
        super();
        this.authCheck = authCheck;
    }
    
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
        Map<String, Object> attributes) throws Exception {
        
        // Log that servletContext is OK
        final ServletContext sc = ((ServletServerHttpRequest) request).getServletRequest().getServletContext();
        if (Objects.isNull(sc)) {
            logger.warn("Warning: servletContext is null at beforeHandshake() entry");
        } else {
            logger.info("ENTRY --> beforeHandshake() -- servletContext attribute {} = {}",
                                    JettyWebSocketServerContainer.JETTY_WEBSOCKET_CONTAINER_ATTRIBUTE,
                                    sc.getAttribute(JettyWebSocketServerContainer.JETTY_WEBSOCKET_CONTAINER_ATTRIBUTE));
        }
        
        // Put request in async mode
        ServerHttpAsyncRequestControl requestControl = request.getAsyncRequestControl(response);
        requestControl.start();

        authCheck.verify("Channel123")
            .thenAccept(result -> {
                logger.info("Authorization complete: {}", result);

                // You should now see that the context is null when we perform the handshake.
                ServletContext sc2 = ((ServletServerHttpRequest) request).getServletRequest().getServletContext();
                if (Objects.isNull(sc2)) {
                    logger.warn("Warning: servletContext is null after authorization.");
                } else {
                    logger.info("after authorization -- servletContext attribute {} = {}",
                                        JettyWebSocketServerContainer.JETTY_WEBSOCKET_CONTAINER_ATTRIBUTE,
                                        sc2.getAttribute(JettyWebSocketServerContainer.JETTY_WEBSOCKET_CONTAINER_ATTRIBUTE));
                }
            
                try {
                    // Perform handshake
                    handshakeHandler.doHandshake(request, response, wsHandler, attributes);                   
                } catch (Exception ex) {
                    logger.error("Exception performing handshake: "+ex);
                }
                // complete request
                requestControl.complete();
                logger.info("Future has completed.");                
            });
        
        logger.info("Main thread continues...");
        return false;
    }

}
