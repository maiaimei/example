package cn.maiaimei.example.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@TestConfiguration
@ComponentScan("cn.maiaimei.example")
@PropertySource("classpath:application.yml")
public class TestContextConfig {
}
