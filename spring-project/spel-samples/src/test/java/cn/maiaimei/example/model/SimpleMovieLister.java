package cn.maiaimei.example.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SimpleMovieLister {

  private String defaultLocale;

  public String getDefaultLocale() {
    return defaultLocale;
  }

  @Autowired
  public void configure(@Value("#{ systemProperties['user.region'] }") String defaultLocale) {
    this.defaultLocale = defaultLocale;
  }
}
