package org.example.websocketdemo2.event;

import org.example.websocketdemo2.endpoint.SampleTextWebSocketEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SampleTextWebSocketEvent {

  @Autowired
  private SampleTextWebSocketEndpoint sampleTextWebSocketEndpoint;

  /**
   * 定时向所有客户端广播消息
   */
  @Scheduled(fixedRate = 10000) // 每10秒执行一次
  public void broadcastMessageToAllClients() {
    sampleTextWebSocketEndpoint.broadcastMessage("Hello, this is a scheduled broadcast message to all clients!");
  }
}
