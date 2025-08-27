package org.example.app.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// java -jar app.jar --spring.profiles.active=server
@SpringBootApplication(scanBasePackages = {
    "org.example.app.server",
    "org.example.common"
})
public class ServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServerApplication.class, args);
  }
}
