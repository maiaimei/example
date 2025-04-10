package org.example.websocketdemo3.endpoint;

import jakarta.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractWebSocketEndpoint {

  protected void addSession(Map<String, ClientSession> clientMap, String clientId, Session session) {
    final ClientSession clientSession = clientMap.get(clientId);
    if (Objects.nonNull(clientSession)) {
      closeSession(clientMap, clientId, clientSession.session);
    }
    clientMap.put(clientId, new ClientSession(clientId, session));
  }

  protected void closeSession(Map<String, ClientSession> clientMap, String clientId, Session session) {
    clientMap.remove(clientId);
    try {
      session.close();
    } catch (IOException e) {
      log.error(String.format("Error in close session %s: %s", session.getId(), e.getMessage()), e);
    }
  }

  protected record ClientSession(String clientId, Session session) {

  }
}
