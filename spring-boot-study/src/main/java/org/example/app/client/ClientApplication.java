package org.example.app.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// java -jar app.jar --spring.profiles.active=client
@SpringBootApplication(scanBasePackages = {
    "org.example.app.client",
    "org.example.common"
})
public class ClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }
}
