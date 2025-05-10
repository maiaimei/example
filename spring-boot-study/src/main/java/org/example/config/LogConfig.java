package org.example.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogConfig {

  @Value("${spring.application.name:unknown}")
  private String applicationName;

  @PostConstruct
  public void init() {
    // 设置系统属性，可以在logback配置中使用
    System.setProperty("applicationName", applicationName);
  }
}
