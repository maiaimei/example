package org.example.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.example.model.domain.Person;
import org.example.mybatis.interceptor.ConditionInterceptor;
import org.example.type.JsonTypeHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement       // 启用事务管理
@MapperScan("org.example.repository")  // 配置Mapper接口扫描路径
public class MyBatisConfig {

  @Autowired
  private ObjectMapper objectMapper;

  @Bean
  public ConfigurationCustomizer configurationCustomizer() {
    return configuration -> {
      configuration.addInterceptor(new ConditionInterceptor());

      final TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
      typeHandlerRegistry.register(Person.class, new JsonTypeHandler<>(objectMapper, Person.class));
      typeHandlerRegistry.register(List.class, new JsonTypeHandler<>(objectMapper,
          new TypeReference<List<Person>>() {
          }));
      typeHandlerRegistry.register(Map.class, new JsonTypeHandler<>(objectMapper,
          new TypeReference<Map<String, Object>>() {
          }));
      typeHandlerRegistry.register(List.class, new JsonTypeHandler<>(objectMapper,
          new TypeReference<List<Map<String, Object>>>() {
          }));
    };
  }
}
