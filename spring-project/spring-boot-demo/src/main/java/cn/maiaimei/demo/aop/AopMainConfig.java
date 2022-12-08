package cn.maiaimei.demo.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@Configuration
public class AopMainConfig {
    @Bean
    public MathService mathService() {
        return new MathService();
    }

    @Bean
    public MathServiceAspect mathServiceAspect() {
        return new MathServiceAspect();
    }
}
