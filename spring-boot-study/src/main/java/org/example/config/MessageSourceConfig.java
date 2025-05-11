package org.example.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class MessageSourceConfig {

  /**
   * 配置字段消息源
   */
  @Bean
  public MessageSource fieldMessageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages/fields");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  /**
   * 配置验证消息源
   */
  @Bean
  public MessageSource validationMessageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages/ValidationMessages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  /**
   * 配置验证器工厂
   */
  @Bean
  public LocalValidatorFactoryBean validator(MessageSource validationMessageSource) {
    LocalValidatorFactoryBean validatorFactory = new LocalValidatorFactoryBean();
    validatorFactory.setValidationMessageSource(validationMessageSource);
    return validatorFactory;
  }

}