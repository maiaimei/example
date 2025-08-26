package cn.maiaimei.config;

import cn.maiaimei.util.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class JsonAutoConfiguration implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @PostConstruct
  public void setUp() {
    ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);
    JsonUtils.setObjectMapper(objectMapper);
  }

}
