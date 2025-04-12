package org.example.websocketdemo1.event;

import org.example.websocketdemo1.handler.SampleTextWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SampleTextWebSocketEvent {

  private final SampleTextWebSocketHandler webSocketHandler;

  @Autowired
  public SampleTextWebSocketEvent(SampleTextWebSocketHandler webSocketHandler) {
    this.webSocketHandler = webSocketHandler;
  }

  /**
   * 定时向所有客户端广播消息
   */
  @Scheduled(fixedRate = 10000) // 每10秒执行一次
  public void broadcastMessageToAllClients() {
    webSocketHandler.broadcastMessage("Hello, this is a scheduled broadcast message to all clients!");
  }
}