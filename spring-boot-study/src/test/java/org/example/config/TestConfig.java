package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@TestConfiguration
@Import(JacksonAutoConfiguration.class)
public class TestConfig {

  @Autowired
  private Jackson2ObjectMapperBuilderCustomizer dateTimeCustomizer;

  @Autowired
  private Jackson2ObjectMapperBuilderCustomizer numberCustomizer;

  /**
   * 配置 ObjectMapper 并应用 JacksonConfig 的两个 bean
   *
   * @return 配置后的 ObjectMapper
   */
  @Bean
  public ObjectMapper objectMapper() {
    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
    dateTimeCustomizer.customize(builder);
    numberCustomizer.customize(builder);
    return builder.build();
  }
}