package cn.maiaimei.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@Slf4j
@SpringBootApplication

public class ClientApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ClientApplication.class, args);
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String port = environment.getProperty("local.server.port");
        String contextPath = environment.getProperty("server.servlet.context-path");
        log.info("http://localhost:{}/{}/swagger-ui/index.html", port, contextPath);
    }
}
