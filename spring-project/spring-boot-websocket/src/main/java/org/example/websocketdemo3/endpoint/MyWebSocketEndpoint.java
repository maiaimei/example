package org.example.websocketdemo3.endpoint;

import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@ServerEndpoint("/ws/{clientId}")
@Component
public class MyWebSocketEndpoint extends AbstractWebSocketEndpoint {

  /**
   * Current number of online connections
   */
  private static final AtomicInteger onlineCount = new AtomicInteger(0);

  private static final Map<String, ClientSession> clientMap = new ConcurrentHashMap<>();

  @OnOpen
  public void onOpen(Session session, EndpointConfig endpointConfig, @PathParam(value = "clientId") String clientId) {
    addSession(clientMap, clientId, session);
    onlineCount.incrementAndGet();
    log.info("New connection opened: {}, clientId: {}, current number of online: {}", session.getId(), clientId, onlineCount.get());
  }

  @OnClose
  public void onClose(Session session, CloseReason closeReason, @PathParam(value = "clientId") String clientId) {
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
