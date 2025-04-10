package org.example.websocketdemo2.endpoint;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@ServerEndpoint("/ws")
@Component
public class MyWebSocketEndpoint {

  // Store all active WebSocket sessions
  private static final CopyOnWriteArraySet<MyWebSocketEndpoint> webSocketSet = new CopyOnWriteArraySet<>();
  private Session session;

  @OnOpen
  public void onOpen(Session session) {
    this.session = session;
    webSocketSet.add(this);
    log.info("New connection opened: {}", session.getId());
    sendMessage("Welcome! Your session ID is " + session.getId());
  }

  @OnMessage
  public void onMessage(String message, Session session) {
    log.info("Received message from session {}: {}", session.getId(), message);

    // Broadcast the message to all connected clients
    for (MyWebSocketEndpoint endpoint : webSocketSet) {
      endpoint.sendMessage("Broadcast from " + session.getId() + ": " + message);
    }
  }

  @OnClose
  public void onClose(Session session) {
    webSocketSet.remove(this);
    log.info("Connection closed: {}", session.getId());
  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    log.error("Error in session {}: {}", session.getId(), throwable.getMessage());
  }

  private void sendMessage(String message) {
    try {
      this.session.getBasicRemote().sendText(message);
    } catch (IOException e) {
      log.error("Error sending message: {}", e.getMessage());
    }
  }

}