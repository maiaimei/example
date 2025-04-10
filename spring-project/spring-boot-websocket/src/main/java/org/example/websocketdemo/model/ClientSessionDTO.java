package org.example.websocketdemo.model;

import jakarta.websocket.Session;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientSessionDTO {

  private String clientId;
  private String sessionId;
  private Session session;
}
