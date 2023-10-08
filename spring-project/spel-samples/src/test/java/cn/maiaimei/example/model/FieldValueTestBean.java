package cn.maiaimei.example.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FieldValueTestBean {

  @Value("#{ systemProperties['user.region'] }")
  private String defaultLocale;

  public void setDefaultLocale(String defaultLocale) {
    this.defaultLocale = defaultLocale;
  }

  public String getDefaultLocale() {
    return this.defaultLocale;
  }
}
