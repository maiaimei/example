package cn.maiaimei.demo;

import cn.maiaimei.demo.listener.XxxApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfigOfApplicationListener {
    @Bean
    public XxxApplicationListener xxxApplicationListener() {
        return new XxxApplicationListener();
    }
}
