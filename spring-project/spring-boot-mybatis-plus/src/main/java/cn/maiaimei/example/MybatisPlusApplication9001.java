package cn.maiaimei.example;

import cn.maiaimei.framework.spring.boot.web.EnableGlobalException;
import cn.maiaimei.framework.spring.boot.web.EnableGlobalResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableGlobalException(isShowTrace = false)
@EnableGlobalResponse
@SpringBootApplication
public class MybatisPlusApplication9001 {
    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusApplication9001.class, args);
    }
}
