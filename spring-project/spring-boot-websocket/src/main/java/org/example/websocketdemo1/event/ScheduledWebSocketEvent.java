package org.example.websocketdemo1.event;

import org.example.websocketdemo1.handler.SampleTextWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledWebSocketEvent {

    private final SampleTextWebSocketHandler webSocketHandler;

    @Autowired
    public ScheduledWebSocketEvent(SampleTextWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    /**
     * 定时向特定客户端发送消息
     */
    @Scheduled(fixedRate = 5000) // 每5秒执行一次
    public void sendMessageToSpecificClient() {
        String sessionId = "specificSessionId"; // 替换为实际的客户端会话ID
        webSocketHandler.sendMessageToClient(sessionId, "Hello, this is a scheduled message to a specific client!");
    }

    /**
     * 定时向所有客户端广播消息
     */
    @Scheduled(fixedRate = 10000) // 每10秒执行一次
    public void broadcastMessageToAllClients() {
        webSocketHandler.broadcastMessage("Hello, this is a scheduled broadcast message to all clients!");
    }
}