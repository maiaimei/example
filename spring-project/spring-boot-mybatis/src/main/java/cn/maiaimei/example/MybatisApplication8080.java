package cn.maiaimei.example;

import cn.maiaimei.framework.web.EnableGlobalException;
import cn.maiaimei.framework.web.EnableGlobalResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableGlobalException
@EnableGlobalResponse
@SpringBootApplication
public class MybatisApplication8080 {
    public static void main(String[] args) {
        SpringApplication.run(MybatisApplication8080.class, args);
    }
}
