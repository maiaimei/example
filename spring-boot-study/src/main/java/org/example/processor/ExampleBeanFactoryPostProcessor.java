package org.example.processor;

import org.example.component.ExampleBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;

@Component
public class ExampleBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    DefaultListableBeanFactory factory = (DefaultListableBeanFactory) beanFactory;

    BeanDefinition beanDefinition = new RootBeanDefinition(ExampleBean.class);
    factory.registerBeanDefinition("exampleBean02", beanDefinition);
  }
}
