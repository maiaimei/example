package cn.maiaimei.example;

import cn.maiaimei.framework.swagger.EnableSwagger;
import cn.maiaimei.framework.web.servlet.EnableGlobalException;
import cn.maiaimei.framework.web.servlet.EnableGlobalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@EnableSwagger
@EnableGlobalResponse
@EnableGlobalException
@SpringBootApplication
public class MybatisApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(MybatisApplication.class, args);
    }

    @Value("${server.port:8080}")
    private String port;

    @Override
    public void run(String... args) throws Exception {
        log.info("H2 started on http://localhost:{}/h2-console", port);
        log.info("Swagger started on http://localhost:{}/swagger-ui/index.html", port);
    }
}
