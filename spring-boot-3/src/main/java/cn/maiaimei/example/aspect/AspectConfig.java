package cn.maiaimei.example.aspect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@Configuration
public class AspectConfig {

  @Bean
  public LoggingAspect loggingAspect() {
    return new LoggingAspect();
  }
}
