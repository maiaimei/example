package cn.maiaimei.example;

import cn.maiaimei.framework.swagger.EnableSwagger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j
@EnableSwagger
@ServletComponentScan
@SpringBootApplication
public class MainApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Value("${server.port:8080}")
    private String port;

    @Override
    public void run(String... args) throws Exception {
        log.info("Swagger started on http://localhost:{}/swagger-ui/index.html", port);
    }
}
