package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Service
public class AuthorizationChecker {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationChecker.class);

    @Autowired
    public AuthorizationChecker() {
    }

    // Check that channel is authorized
    public CompletableFuture<String> verify(String channelId) throws InterruptedException {
        CompletableFuture<String> myCF = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            logger.info("AuthorizationChecker verifying channel {}", channelId);
            Thread.sleep(5000);
            myCF.complete(channelId + "is authorized.");
            return null;
        });

        return myCF;            
    }
}
