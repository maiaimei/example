package org.example.websocketdemo1.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
public class SampleTextWebSocketHandler extends TextWebSocketHandler {

  // Store active WebSocket sessions
  private final ConcurrentHashMap<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    String clientMessage = message.getPayload();
    log.info("Received message from session {}: {}", session.getId(), clientMessage);

    // Echo the message back to the client
    session.sendMessage(new TextMessage("Response message from SampleTextWebSocketHandler: " + clientMessage));
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    log.info("New connection opened: {}", session.getId());
    activeSessions.put(session.getId(), session); // Add session to active sessions
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    log.info("Connection closed: {}, {}", session.getId(), status.toString());
    activeSessions.remove(session.getId()); // Remove session from active sessions
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    log.error("Error in session {}: {}", session.getId(), exception.getMessage());
  }

  /**
   * Send a message to a specific client by session ID.
   *
   * @param sessionId the ID of the session to send the message to
   * @param message   the message to send
   */
  public void sendMessageToClient(String sessionId, String message) {
    WebSocketSession session = activeSessions.get(sessionId);
    if (session != null && session.isOpen()) {
      try {
        session.sendMessage(new TextMessage(message));
        log.info("Message sent to session {}: {}", sessionId, message);
      } catch (Exception e) {
        log.error("Failed to send message to session {}: {}", sessionId, e.getMessage());
      }
    } else {
      log.warn("Session {} is not open or does not exist", sessionId);
    }
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
          log.info("Broadcast message sent to session {}: {}", session.getId(), message);
        } catch (Exception e) {
          log.error("Failed to send broadcast message to session {}: {}", session.getId(), e.getMessage());
        }
      }
    });
  }
}