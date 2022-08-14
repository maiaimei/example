package cn.maiaimei.example.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableFeignClients(basePackages = "cn.maiaimei.example.client")
public class FeignConfig {
}