package org.example.common.registrar;

import org.example.common.component.ExampleBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class ExampleImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

  @Override
  public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
    BeanDefinition beanDefinition = new RootBeanDefinition(ExampleBean.class);
    registry.registerBeanDefinition("exampleBean04", beanDefinition);
  }
}
