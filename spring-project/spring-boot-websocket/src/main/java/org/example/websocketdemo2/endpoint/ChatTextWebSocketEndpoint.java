package org.example.websocketdemo2.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@ServerEndpoint("/ws/chat/{userId}")
@Component
public class ChatTextWebSocketEndpoint {

  // JSON parser
  private static final ObjectMapper objectMapper = new ObjectMapper();

  // Store active WebSocket sessions with userId as the key
  private static final ConcurrentHashMap<String, Session> activeSessions = new ConcurrentHashMap<>();

  @OnOpen
  public void onOpen(Session session, EndpointConfig endpointConfig, @PathParam("userId") String userId) {
    log.info("New connection opened: userId={}, sessionId={}", userId, session.getId());
    activeSessions.put(userId, session); // Add session to active sessions using userId
  }

  @OnMessage
  public void onMessage(String message, Session session) throws JsonProcessingException {
    Map<String, String> messageData = objectMapper.readValue(message, new TypeReference<Map<String, String>>() {
    });
    String fromUserId = messageData.get("fromUserId");
    String toUserId = messageData.get("toUserId");
    String chatMessage = messageData.get("message");

    log.info("Received message from userId {}: {}", fromUserId, chatMessage);

    Map<String, String> chatMessageData = Map.of(
        "fromUserId", fromUserId,
        "message", chatMessage);

    sendMessage(activeSessions.get(toUserId), objectMapper.writeValueAsString(chatMessageData));
  }

  @OnClose
  public void onClose(Session session, CloseReason closeReason, @PathParam("userId") String userId) {
    log.info("Connection closed: userId={}, sessionId={}, status={}", userId, session.getId(), closeReason.toString());
    activeSessions.remove(session.getId());
  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    log.error("Error in session {}: {}", session.getId(), throwable.getMessage());
  }

  private void sendMessage(Session session, String message) {
    try {
      session.getBasicRemote().sendText(message);
    } catch (IOException e) {
      log.error("Error sending message: {}", e.getMessage());
    }
  }

}
