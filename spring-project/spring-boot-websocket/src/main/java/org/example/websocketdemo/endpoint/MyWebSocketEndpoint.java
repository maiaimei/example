package org.example.websocketdemo.endpoint;

import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@ServerEndpoint("/ws-test/{clientId}")
@Component
public class MyWebSocketEndpoint {

  private static final String sessionPrefix = "ws-test-";

  /**
   * Current number of online connections
   */
  private static final AtomicInteger onlineCount = new AtomicInteger(0);

  @OnOpen
  public void onOpen(Session session, EndpointConfig endpointConfig, @PathParam(value = "clientId") String clientId) {
    onlineCount.incrementAndGet();
    log.info("New connection opened: {}, clientId: {}, current number of online: {}", session.getId(), clientId, onlineCount.get());
  }

  @OnClose
  public void onClose(Session session, CloseReason closeReason) {
    onlineCount.decrementAndGet();
    log.info("Connection closed: {}, closeReason: {}, current number of online: {}", session.getId(),
        closeReason.toString(), onlineCount.get());
  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    log.error("Error in session {}: {}", session.getId(), throwable.getMessage());
  }

  @OnMessage
  public void onMessage(String message, Session session) {
    log.info("Received message from session {}: {}", session.getId(), message);
  }

}
