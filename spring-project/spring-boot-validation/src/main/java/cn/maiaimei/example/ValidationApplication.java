package cn.maiaimei.example;

import cn.maiaimei.framework.spring.boot.web.EnableGlobalException;
import cn.maiaimei.framework.spring.boot.web.EnableGlobalResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableGlobalException
@EnableGlobalResponse
@SpringBootApplication
public class ValidationApplication {
    public static void main(String[] args) {
        SpringApplication.run(ValidationApplication.class, args);
    }
}
