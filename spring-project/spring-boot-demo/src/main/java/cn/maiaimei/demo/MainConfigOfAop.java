package cn.maiaimei.demo;

import cn.maiaimei.demo.service.MathService;
import cn.maiaimei.demo.service.MathServiceAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * https://www.bilibili.com/video/BV1gW411W7wy?p=31
 */
@EnableAspectJAutoProxy
@Configuration
public class MainConfigOfAop {
    @Bean
    public MathService mathService() {
        return new MathService();
    }

    @Bean
    public MathServiceAspect mathServiceAspect() {
        return new MathServiceAspect();
    }
}
