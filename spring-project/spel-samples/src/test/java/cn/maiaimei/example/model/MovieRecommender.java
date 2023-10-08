package cn.maiaimei.example.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MovieRecommender {

  private String defaultLocale;

  public String getDefaultLocale() {
    return defaultLocale;
  }

  public MovieRecommender(@Value("#{systemProperties['user.country']}") String defaultLocale) {
    this.defaultLocale = defaultLocale;
  }
}
