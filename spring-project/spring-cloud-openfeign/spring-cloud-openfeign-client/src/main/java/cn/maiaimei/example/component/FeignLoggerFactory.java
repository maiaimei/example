package cn.maiaimei.example.component;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignLoggerFactory implements org.springframework.cloud.openfeign.FeignLoggerFactory {

    private Logger logger;

    public FeignLoggerFactory() {
    }

    public FeignLoggerFactory(Logger logger) {
        this.logger = logger;
    }

    @Override
    public Logger create(Class<?> type) {
        return this.logger != null ? this.logger : new FeignLogger();
    }

    /**
     * 开启openfeign的日志
     * 因为使用自定义日志，所以开启（最低级别）即可
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }
}