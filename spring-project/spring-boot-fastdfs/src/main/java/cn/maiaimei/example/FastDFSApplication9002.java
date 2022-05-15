package cn.maiaimei.example;

import cn.maiaimei.framework.spring.boot.web.EnableGlobalException;
import cn.maiaimei.framework.spring.boot.web.EnableGlobalResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableGlobalException
@EnableGlobalResponse
@SpringBootApplication
public class FastDFSApplication9002 {
    public static void main(String[] args) {
        SpringApplication.run(FastDFSApplication9002.class, args);
    }
}
