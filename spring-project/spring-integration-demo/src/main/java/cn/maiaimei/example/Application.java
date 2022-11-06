package cn.maiaimei.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.config.EnableIntegration;


@ImportResource(locations = {
        //"classpath:config/hello-integration-config.xml"
        //"classpath:config/http-inbound-config.xml"
        //"classpath:config/http-outbound-config.xml"
})
@EnableIntegration
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
