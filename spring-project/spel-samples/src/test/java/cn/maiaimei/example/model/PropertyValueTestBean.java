package cn.maiaimei.example.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyValueTestBean {

  private String defaultLocale;

  @Value("#{ systemProperties['user.region'] }")
  public void setDefaultLocale(String defaultLocale) {
    this.defaultLocale = defaultLocale;
  }

  public String getDefaultLocale() {
    return this.defaultLocale;
  }
}
