package org.example.common.processor;

import org.example.common.component.ExampleBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;

@Component
public class ExampleBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
    BeanDefinition beanDefinition = new RootBeanDefinition(ExampleBean.class);
    registry.registerBeanDefinition("exampleBean03", beanDefinition);
  }
}
