package org.example.factory;

import org.example.component.ExampleBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

@Component
public class ExampleBeanFactoryBean implements FactoryBean<ExampleBean> {

  @Override
  public ExampleBean getObject() throws Exception {
    return new ExampleBean();
  }

  @Override
  public Class<?> getObjectType() {
    return ExampleBean.class;
  }

}
