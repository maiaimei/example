package org.example.websocketdemo1.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
public class MyWebSocketHandler extends TextWebSocketHandler {

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    String clientMessage = message.getPayload();
    log.info("Received message from session {}: {}", session.getId(), clientMessage);

    // Echo the message back to the client
    session.sendMessage(new TextMessage("Server response: " + clientMessage));
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    log.info("New connection opened: {}", session.getId());
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    log.info("Connection closed: {}, {}", session.getId(), status.toString());
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    log.error("Error in session {}: {}", session.getId(), exception.getMessage());
  }

}