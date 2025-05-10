package org.example.config;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageSourceConfig {

    @Bean
    public MessageSource responseMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("ResponseMessages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(3600); // 缓存 1 小时
        return messageSource;
    }

    @Bean
    public MessageSource fieldMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("fields");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(3600); // 缓存 1 小时
        return messageSource;
    }

}