package cn.maiaimei.example;

import cn.maiaimei.framework.web.servlet.EnableGlobalException;
import cn.maiaimei.framework.web.servlet.EnableGlobalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

@Slf4j
@EnableGlobalResponse
@EnableGlobalException
@SpringBootApplication
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Value("${swagger.enabled:false}")
    private boolean enabledSwagger;

    @Value("${server.port:8080}")
    private String port;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Override
    public void run(String... args) {
        if (enabledSwagger) {
            if (StringUtils.hasText(contextPath)) {
                contextPath = "/".concat(contextPath);
            } else {
                contextPath = "";
            }
            log.info("Swagger started on http://localhost:{}{}/swagger-ui/index.html", port, contextPath);
        }
    }
}
