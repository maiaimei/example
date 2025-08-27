package org.example.common.config;

import org.example.common.component.ExampleBean;
import org.example.common.registrar.ExampleImportBeanDefinitionRegistrar;
import org.example.common.registrar.ExampleImportSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(value = {
    ExampleBean.class, // 导入普通类
    ExampleImportBeanDefinitionRegistrar.class, // 导入实现ImportBeanDefinitionRegistrar接口的类
    ExampleImportSelector.class
})
@Configuration
public class ExampleConfiguration {

  @Bean(name = "exampleBean01")
  public ExampleBean exampleBean() {
    return new ExampleBean();
  }
}
