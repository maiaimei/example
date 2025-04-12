package org.example.websocketdemo1.config;

import org.example.websocketdemo1.handler.ExampleTextWebSocketHandler;
import org.example.websocketdemo1.handler.OnlineChatTextWebSocketHandler;
import org.example.websocketdemo1.handler.SampleTextWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(new SampleTextWebSocketHandler(), "/ws/sample/text").setAllowedOrigins("*");
    registry.addHandler(new ExampleTextWebSocketHandler(), "/ws/example/text").setAllowedOrigins("*");
    registry.addHandler(new OnlineChatTextWebSocketHandler(), "/ws/online-chat").setAllowedOrigins("*");
  }

}