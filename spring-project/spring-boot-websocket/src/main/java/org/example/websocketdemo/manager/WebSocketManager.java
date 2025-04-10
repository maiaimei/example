package org.example.websocketdemo.manager;

import jakarta.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.example.websocketdemo.model.ClientSessionDTO;

public class WebSocketManager {

  private static final Map<String, ClientSessionDTO> clientSessionMap = new ConcurrentHashMap<>();

  public static void addSession(String sessionPrefix, Session session, String clientId) {
    clientSessionMap.put(sessionPrefix + session.getId(), new ClientSessionDTO(clientId, session.getId(), session));
  }
}
