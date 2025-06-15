package org.example.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.example.model.domain.Person;
import org.example.model.domain.Role;
import org.example.mybatis.interceptor.ConditionInterceptor;
import org.example.type.ArrayTypeHandler;
import org.example.type.JsonTypeHandler;
import org.example.type.ListTypeHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@Configuration
@EnableTransactionManagement       // 启用事务管理
@MapperScan("org.example.repository")  // 配置Mapper接口扫描路径
public class MyBatisConfig {

  @Autowired
  private ObjectMapper objectMapper;

  @Bean
  public ConfigurationCustomizer configurationCustomizer() {
    return configuration -> {
      final TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

      // 添加拦截器
      configuration.addInterceptor(new ConditionInterceptor());

      // 按顺序注册 TypeHandler
      registerTypeHandlers(typeHandlerRegistry);
    };
  }

  private void registerTypeHandlers(TypeHandlerRegistry registry) {
    try {
      // 1. 注册基本对象的 TypeHandler
      registerBasicTypeHandlers(registry);

      // 2. 注册简单集合类型的 TypeHandler
      registerSimpleCollectionTypeHandlers(registry);

      // 3. 注册数组类型的 TypeHandler
      registerArrayTypeHandlers(registry);

      // 4. 注册复杂集合类型的 TypeHandler（从具体到抽象）
      //registerComplexCollectionTypeHandlers(registry);

      // 5. 注册Map类型的 TypeHandler
      //registerMapTypeHandlers(registry);

    } catch (Exception e) {
      log.error("Failed to register type handlers", e);
      throw new RuntimeException("TypeHandler registration failed", e);
    }
  }

  private void registerBasicTypeHandlers(TypeHandlerRegistry registry) {
    registry.register(Person.class, new JsonTypeHandler<>(objectMapper, Person.class));
    registry.register(Role.class, new JsonTypeHandler<>(objectMapper, Role.class));
  }

  private void registerSimpleCollectionTypeHandlers(TypeHandlerRegistry registry) {
    registry.register(List.class, new ListTypeHandler<>(String.class));
    registry.register(List.class, new ListTypeHandler<>(BigDecimal.class));
    registry.register(List.class, new ListTypeHandler<>(Integer.class));
  }

  private void registerArrayTypeHandlers(TypeHandlerRegistry registry) {
    registry.register(String[].class, new ArrayTypeHandler<>(String.class));
    registry.register(BigDecimal[].class, new ArrayTypeHandler<>(BigDecimal.class));
    registry.register(Integer[].class, new ArrayTypeHandler<>(Integer.class));
  }

  private void registerComplexCollectionTypeHandlers(TypeHandlerRegistry registry) {
    registry.register(List.class, new JsonTypeHandler<>(objectMapper,
        new TypeReference<List<Person>>() {
        }));
    registry.register(List.class, new JsonTypeHandler<>(objectMapper,
        new TypeReference<List<Role>>() {
        }));
  }

  private void registerMapTypeHandlers(TypeHandlerRegistry registry) {
    registry.register(Map.class, new JsonTypeHandler<>(objectMapper,
        new TypeReference<Map<String, Object>>() {
        }));
    // 6. 最后注册最通用的集合类型 TypeHandler
    registry.register(List.class, new JsonTypeHandler<>(objectMapper,
        new TypeReference<List<Map<String, Object>>>() {
        }));
  }


}
