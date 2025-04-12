package org.example.websocketdemo2.endpoint;

import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@ServerEndpoint("/ws/sample-text")
@Component
public class SampleTextWebSocketEndpoint {

  // Store active sessions
  private static final ConcurrentHashMap<String, Session> activeSessions = new ConcurrentHashMap<>();

  @OnOpen
  public void onOpen(Session session, EndpointConfig endpointConfig) {
    log.info("New connection opened: {}", session.getId());
    activeSessions.put(session.getId(), session);
    sendMessage(session, String.format("Welcome! Your session ID is %s", session.getId()));
  }

  @OnMessage
  public void onMessage(String message, Session session) {
    log.info("Received message from session {}: {}", session.getId(), message);

    // Echo the message back to the client
    sendMessage(session, String.format("Response message from SampleTextWebSocketEndpoint: %s", message));
  }

  @OnClose
  public void onClose(Session session, CloseReason closeReason) {
    log.info("Connection closed: {} {}", session.getId(), closeReason.toString());
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

  /**
   * Broadcast a message to all connected clients.
   *
   * @param message the message to broadcast
   */
  public void broadcastMessage(String message) {
    activeSessions.values().forEach(session -> {
      if (session.isOpen()) {
        try {
          sendMessage(session, message);
          log.info("Broadcast message sent to {}: {}", session.getId(), message);
        } catch (Exception e) {
          log.error("Failed to send broadcast message to {}: {}", session.getId(), e.getMessage());
        }
      }
    });
  }

}