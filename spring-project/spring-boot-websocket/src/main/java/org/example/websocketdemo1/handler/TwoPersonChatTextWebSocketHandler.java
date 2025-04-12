package org.example.websocketdemo1.handler;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
public class TwoPersonChatTextWebSocketHandler extends TextWebSocketHandler {

  // Store active WebSocket sessions with userId as the key
  private static final ConcurrentHashMap<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    String clientMessage = message.getPayload();
    log.info("Received message from session {}: {}", session.getId(), clientMessage);

    final String targetUserId = getTargetUserIdFromSession(session);
    sendMessageToClient(targetUserId, clientMessage);
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    // Assume userId is passed as a query parameter in the WebSocket URL
    String userId = getCurrentLoginUserIdFromSession(session);
    if (userId != null) {
      log.info("New connection opened: userId={}, sessionId={}", userId, session.getId());
      activeSessions.put(userId, session); // Add session to active sessions using userId
    } else {
      log.warn("Connection rejected: userId is missing for session {}", session.getId());
      session.close(CloseStatus.BAD_DATA);
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    String userId = getCurrentLoginUserIdFromSession(session);
    if (userId != null) {
      log.info("Connection closed: userId={}, sessionId={}, status={}", userId, session.getId(), status);
      activeSessions.remove(userId); // Remove session from active sessions
    }
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    log.error("Error in session {}: {}", session.getId(), exception.getMessage());
  }

  /**
   * Send a message to a specific client by userId.
   *
   * @param userId  the ID of the client to send the message to
   * @param message the message to send
   */
  public void sendMessageToClient(String userId, String message) {
    if (Objects.isNull(userId)) {
      log.error("Failed to send message: userId is missing");
      return;
    }
    WebSocketSession session = activeSessions.get(userId);
    if (session != null && session.isOpen()) {
      try {
        session.sendMessage(new TextMessage(message));
        log.info("Message sent to userId {}: {}", userId, message);
      } catch (Exception e) {
        log.error("Failed to send message to userId {}: {}", userId, e.getMessage());
      }
    } else {
      log.warn("userId {} is not connected or session is closed", userId);
    }
  }

  /**
   * Extract userId from the WebSocket session.
   *
   * @param session the WebSocket session
   * @return the userId, or null if not found
   */
  private String getCurrentLoginUserIdFromSession(WebSocketSession session) {
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
    if (Objects.isNull(userId)) {
      userId = session.getId();
    }
    return userId;
  }

  /**
   * Extract targetUserId from the WebSocket session.
   *
   * @param session the WebSocket session
   * @return the userId, or null if not found
   */
  private String getTargetUserIdFromSession(WebSocketSession session) {
    String userId = null;
    // Assume userId is passed as a query parameter in the WebSocket URL
    String query = session.getUri().getQuery();
    if (query != null) {
      for (String param : query.split("&")) {
        String[] keyValue = param.split("=");
        if (keyValue.length == 2 && "targetUserId".equals(keyValue[0])) {
          userId = keyValue[1];
        }
      }
    }
    return userId;
  }

}
