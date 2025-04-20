package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(value = {ExampleConfig.class})
@Configuration
public class ExampleConfiguration {

  @Bean
  public ExampleBean exampleBean(){
    return new ExampleBean();
  }
}
