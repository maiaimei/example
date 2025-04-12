package org.example.websocketdemo1.handler;

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
 * 在 Spring 中，@Component 默认会将类的实例注册为单例（Singleton）作用域的 Bean。 这意味着通过 @Autowired 注入的 SampleTextWebSocketHandler 是一个全局共享的实例， 而 WebSocket
 * 连接是由 Spring WebSocket 框架单独管理的， 每个客户端连接都会创建一个新的 WebSocketSession。 如果你发现注入的 SampleTextWebSocketHandler 实例与客户端连接的 WebSocket 不一致，
 * 可能是因为你误解了 SampleTextWebSocketHandler 的作用。 SampleTextWebSocketHandler 是一个处理 WebSocket 消息的类，而不是直接代表某个客户端连接。
 * </p>
 */
@Slf4j
@Component
public class SampleTextWebSocketHandler extends TextWebSocketHandler {

  // Store active WebSocket sessions
  private static final ConcurrentHashMap<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    String clientMessage = message.getPayload();
    log.info("Received message from session {}: {}", session.getId(), clientMessage);

    // Echo the message back to the client
    session.sendMessage(new TextMessage(String.format("Response message from SampleTextWebSocketHandler: %s", clientMessage)));
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    log.info("New connection opened: {}", session.getId());
    activeSessions.put(session.getId(), session);
    session.sendMessage(new TextMessage(String.format("Welcome! Your session ID is %s", session.getId())));
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    log.info("Connection closed: {} {}", session.getId(), status.toString());
    activeSessions.remove(session.getId());
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    log.error("Error in session {}: {}", session.getId(), exception.getMessage());
  }

  /**
   * Broadcast a message to all connected clients.
   *
   * @param message the message to broadcast
   */
  public void broadcastMessage(String message) {
    activeSessions.values().forEach(session -> {
      if (session.isOpen()) {
        try {
          session.sendMessage(new TextMessage(message));
          log.info("Broadcast message sent to {}: {}", session.getId(), message);
        } catch (Exception e) {
          log.error("Failed to send broadcast message to {}: {}", session.getId(), e.getMessage());
        }
      }
    });
  }

}