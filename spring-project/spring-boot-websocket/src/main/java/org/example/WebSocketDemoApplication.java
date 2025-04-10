package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.example.websocketdemo2")
public class WebSocketDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebSocketDemoApplication.class, args);
  }
}
