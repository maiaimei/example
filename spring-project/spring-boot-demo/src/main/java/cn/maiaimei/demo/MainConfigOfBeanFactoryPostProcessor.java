package cn.maiaimei.demo;

import cn.maiaimei.demo.processor.XxxBeanDefinitionRegistryPostProcessor;
import cn.maiaimei.demo.processor.XxxBeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfigOfBeanFactoryPostProcessor {
    @Bean
    public XxxBeanDefinitionRegistryPostProcessor xxxBeanDefinitionRegistryPostProcessor() {
        return new XxxBeanDefinitionRegistryPostProcessor();
    }

    @Bean
    public XxxBeanFactoryPostProcessor xxxBeanFactoryPostProcessor() {
        return new XxxBeanFactoryPostProcessor();
    }
}
