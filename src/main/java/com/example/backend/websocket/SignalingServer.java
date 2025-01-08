package com.example.backend.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SignalingServer extends TextWebSocketHandler {

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper(); // Create a single ObjectMapper instance

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("New WebSocket connection: " + session.getId());
        sessions.put(session.getId(), session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Map<String, Object> payload = objectMapper.readValue(message.getPayload(), Map.class);
        String type = (String) payload.get("type");

        if ("chat".equals(type)) {
            handleChatMessage(session, payload);
        } else if ("offer".equals(type) || "answer".equals(type) || "candidate".equals(type)) {
            handleSignalingMessage(session, payload);
        } else {
            System.err.println("Unknown message type received: " + type);
        }
    }

    private void handleChatMessage(WebSocketSession sender, Map<String, Object> payload) {
        String message = (String) payload.get("message");
        String meetingId = (String) payload.get("meetingId");
        String senderUsername = (String) payload.get("user");

        sessions.forEach((sessionId, session) -> {
            try {
                if (session.isOpen()) {
                    Map<String, Object> chatPayload = new HashMap<>();
                    chatPayload.put("type", "chat");
                    chatPayload.put("meetingId", meetingId);
                    chatPayload.put("user", senderUsername);
                    chatPayload.put("message", message);

                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatPayload)));
                }
            } catch (IOException e) {
                System.err.println("Error sending chat message: " + e.getMessage());
            }
        });
    }


    private void handleSignalingMessage(WebSocketSession sender, Map<String, Object> payload) {
        sessions.forEach((sessionId, session) -> {
            try {
                if (!session.equals(sender) && session.isOpen()) {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(payload)));
                }
            } catch (IOException e) {
                System.err.println("Error forwarding signaling message to session " + sessionId + ": " + e.getMessage());
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        System.out.println("WebSocket connection closed: " + session.getId());
        sessions.remove(session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err.println("Error in WebSocket session " + session.getId() + ": " + exception.getMessage());
    }
}
