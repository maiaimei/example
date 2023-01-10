package cn.maiaimei.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validator;
import java.nio.charset.StandardCharsets;

/**
 * 国际化失败消息
 * 如果需要自定义国际化文件名和位置，则增加如下配置类
 * <p>或者配置yml，详见application-i18n.yml</p>
 */
@Configuration
public class MessageConfig {

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource resourceBundle = new ResourceBundleMessageSource();
        resourceBundle.setDefaultEncoding(StandardCharsets.UTF_8.name());
        // 指定国际化文件路径
        resourceBundle.setBasenames("i18n/ValidationMessages", "i18n/messages");
        return resourceBundle;
    }

    @Bean
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource());
        return validator;
    }

    @Bean
    public MethodValidationPostProcessor validationPostProcessor() {
        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
        // 指定请求验证器
        processor.setValidator(getValidator());
        return processor;
    }
}
