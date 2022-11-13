package cn.maiaimei.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                //"cn.maiaimei.example.single" // 单数据源
                "cn.maiaimei.example.multiple" // 多数据源
        }
)
public class JpaApplication {
    public static void main(String[] args) {
        SpringApplication.run(JpaApplication.class, args);
    }
}
