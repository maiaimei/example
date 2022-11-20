package cn.maiaimei.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(
        scanBasePackages = {
                "cn.maiaimei.example.single" // 单数据源
                //"cn.maiaimei.example.multiple" // 多数据源
        }
)
public class JpaApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(JpaApplication.class, args);
    }

    @Value("${server.port:8080}")
    private String port;

    @Override
    public void run(String... args) throws Exception {
        log.info("http://localhost:{}/h2-console", port);
        log.info("http://localhost:{}/swagger-ui/index.html", port);
    }
}
