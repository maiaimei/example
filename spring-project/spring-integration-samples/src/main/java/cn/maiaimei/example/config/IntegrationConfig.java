package cn.maiaimei.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:integration/*.xml")
public class IntegrationConfig {
}
