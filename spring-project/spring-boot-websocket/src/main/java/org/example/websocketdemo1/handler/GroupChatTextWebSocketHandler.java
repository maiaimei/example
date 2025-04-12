package org.example.websocketdemo1.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
public class GroupChatTextWebSocketHandler extends TextWebSocketHandler {

  // JSON parser
  private static final ObjectMapper objectMapper = new ObjectMapper();

  // Store active WebSocket sessions with userId as the key
  private static final ConcurrentHashMap<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    // 解析收到的消息
    String payload = message.getPayload();
    log.info("Received message: {}", payload);

    // 假设消息格式为 JSON
    Map<String, String> messageData = objectMapper.readValue(payload, new TypeReference<Map<String, String>>() {
    });
    String userId = messageData.get("userId");
    String userName = messageData.get("userName");
    String chatMessage = messageData.get("message");
    // 封装收到的消息
    Map<String, String> chatMessageData = Map.of(
        "userId", userId,
        "userName", userName,
        "message", chatMessage);

    activeSessions.forEach((activeSessionUid, activeSession) -> {
      if (Objects.nonNull(activeSession) && activeSession.isOpen() && !activeSessionUid.equalsIgnoreCase(userId)) {
        try {
          activeSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessageData)));
          log.info("Broadcast message sent to userId {}: {}", activeSessionUid, message);
        } catch (Exception e) {
          log.error("Failed to send broadcast message to userId {}: {}", activeSessionUid, e.getMessage());
        }
      }
    });
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    // Assume userId is passed as a query parameter in the WebSocket URL
    String userId = getUserIdFromSession(session);
    if (Objects.nonNull(userId)) {
      log.info("New connection opened: userId={}, sessionId={}", userId, session.getId());
      activeSessions.put(userId, session); // Add session to active sessions using userId
    } else {
      log.warn("Connection rejected: userId is missing for session {}", session.getId());
      session.close(CloseStatus.BAD_DATA);
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    String userId = getUserIdFromSession(session);
    if (Objects.nonNull(userId)) {
      log.info("Connection closed: userId={}, sessionId={}, status={}", userId, session.getId(), status);
      activeSessions.remove(userId); // Remove session from active sessions
    }
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    log.error("Error in session {}: {}", session.getId(), exception.getMessage());
  }

  /**
   * Extract userId from the WebSocket session.
   *
   * @param session the WebSocket session
   * @return the userId, or null if not found
   */
  private String getUserIdFromSession(WebSocketSession session) {
    String userId = null;
    // Assume userId is passed as a query parameter in the WebSocket URL
    String query = session.getUri().getQuery();
    if (query != null) {
      for (String param : query.split("&")) {
        String[] keyValue = param.split("=");
        if (keyValue.length == 2 && "userId".equals(keyValue[0])) {
          userId = keyValue[1];
        }
      }
    }
    if (Objects.isNull(userId)) {
      // Extract userId from the session headers
      if (session.getHandshakeHeaders().containsKey("userId")) {
        userId = session.getHandshakeHeaders().getFirst("userId");
      }
    }
    return userId;
  }
}
