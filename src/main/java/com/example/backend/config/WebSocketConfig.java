package com.example.backend.config;

import com.example.backend.websocket.SignalingServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final SignalingServer signalingServer;

    public WebSocketConfig(SignalingServer signalingServer) {
        this.signalingServer = signalingServer;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(signalingServer, "/signaling").setAllowedOrigins("*");
    }
}
