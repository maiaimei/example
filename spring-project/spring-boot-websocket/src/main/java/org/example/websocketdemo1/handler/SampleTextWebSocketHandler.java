package org.example.websocketdemo1.handler;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Sample WebSocket handler for handling text messages.
 * <p>
 * This handler manages WebSocket sessions and provides methods to send messages
 * to specific clients
 * or broadcast messages to all connected clients.
 * </p>
 * <p>
 * 在 Spring 中，@Component 默认会将类的实例注册为单例（Singleton）作用域的 Bean。
 * 这意味着通过 @Autowired 注入的 SampleTextWebSocketHandler 是一个全局共享的实例，
 * 而 WebSocket 连接是由 Spring WebSocket 框架单独管理的，
 * 每个客户端连接都会创建一个新的 WebSocketSession。
 * 如果你发现注入的 SampleTextWebSocketHandler 实例与客户端连接的 WebSocket 不一致，
 * 可能是因为你误解了 SampleTextWebSocketHandler 的作用。
 * SampleTextWebSocketHandler 是一个处理 WebSocket 消息的类，而不是直接代表某个客户端连接。
 * </p>
 */
@Slf4j
@Component
public class SampleTextWebSocketHandler extends TextWebSocketHandler {

  // Store active WebSocket sessions with clientId as the key
  private static final ConcurrentHashMap<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    String clientMessage = message.getPayload();
    log.info("Received message from session {}: {}", session.getId(), clientMessage);

    // Echo the message back to the client
    session.sendMessage(new TextMessage("Response message from SampleTextWebSocketHandler: " + clientMessage));
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    // Assume clientId is passed as a query parameter in the WebSocket URL
    String clientId = getClientIdFromSession(session);
    if (clientId != null) {
      log.info("New connection opened: clientId={}, sessionId={}", clientId, session.getId());
      activeSessions.put(clientId, session); // Add session to active sessions using clientId
    } else {
      log.warn("Connection rejected: clientId is missing for session {}", session.getId());
      session.close(CloseStatus.BAD_DATA);
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    String clientId = getClientIdFromSession(session);
    if (clientId != null) {
      log.info("Connection closed: clientId={}, sessionId={}, status={}", clientId, session.getId(), status);
      activeSessions.remove(clientId); // Remove session from active sessions
    }
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    log.error("Error in session {}: {}", session.getId(), exception.getMessage());
  }

  /**
   * Send a message to a specific client by clientId.
   *
   * @param clientId the ID of the client to send the message to
   * @param message  the message to send
   */
  public void sendMessageToClient(String clientId, String message) {
    WebSocketSession session = activeSessions.get(clientId);
    if (session != null && session.isOpen()) {
      try {
        session.sendMessage(new TextMessage(message));
        log.info("Message sent to clientId {}: {}", clientId, message);
      } catch (Exception e) {
        log.error("Failed to send message to clientId {}: {}", clientId, e.getMessage());
      }
    } else {
      log.warn("ClientId {} is not connected or session is closed", clientId);
    }
  }

  /**
   * Broadcast a message to all connected clients.
   *
   * @param message the message to broadcast
   */
  public void broadcastMessage(String message) {
    activeSessions.forEach((clientId, session) -> {
      if (session.isOpen()) {
        try {
          session.sendMessage(new TextMessage(message));
          log.info("Broadcast message sent to clientId {}: {}", clientId, message);
        } catch (Exception e) {
          log.error("Failed to send broadcast message to clientId {}: {}", clientId, e.getMessage());
        }
      }
    });
  }

  /**
   * Extract clientId from the WebSocket session.
   *
   * @param session the WebSocket session
   * @return the clientId, or null if not found
   */
  private String getClientIdFromSession(WebSocketSession session) {
    String clientId = null;
    // Assume clientId is passed as a query parameter in the WebSocket URL
    String query = session.getUri().getQuery();
    if (query != null) {
      for (String param : query.split("&")) {
        String[] keyValue = param.split("=");
        if (keyValue.length == 2 && "clientId".equals(keyValue[0])) {
          clientId = keyValue[1];
        }
      }
    }
    if (Objects.isNull(clientId)) {
      // Extract clientId from the session headers
      if (session.getHandshakeHeaders().containsKey("clientId")) {
        clientId = session.getHandshakeHeaders().getFirst("clientId");
      }
    }
    if (Objects.isNull(clientId)) {
      clientId = session.getId();
    }
    return clientId;
  }
}