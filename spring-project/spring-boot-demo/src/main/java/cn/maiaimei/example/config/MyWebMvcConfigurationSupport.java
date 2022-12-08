package cn.maiaimei.example.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@ConditionalOnExpression("false")
@Configuration
public class MyWebMvcConfigurationSupport extends WebMvcConfigurationSupport {
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        AntPathMatcher matcher = new AntPathMatcher();
        // 配置URL忽略大小写，解决 405 Method Not Allowed
        matcher.setCaseSensitive(false);
        configurer.setPathMatcher(matcher);
    }
}
