package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.example.model.domain.ConcreteElementA;
import org.example.mybatis.interceptor.ConditionInterceptor;
import org.example.mybatis.type.*;
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
  public ConfigurationCustomizer configurationCustomizer() { //TypeHandlerInterceptor typeHandlerInterceptor
    return configuration -> {
      final TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

      // 添加拦截器
      configuration.addInterceptor(new ConditionInterceptor());
      //configuration.addInterceptor(typeHandlerInterceptor);

      // 按顺序注册 TypeHandler
      registerTypeHandlers(typeHandlerRegistry);
    };
  }

  /**
   * MyBatis 的类型处理器注册应该遵循从具体到抽象的顺序，避免后面的处理器覆盖前面的处理器
   */
  private void registerTypeHandlers(TypeHandlerRegistry registry) {
    try {
      log.debug("Starting type handlers registration");
      registerJsonTypeHandlers(registry);
      registerArrayTypeHandlers(registry);
      log.info("Successfully registered all type handlers");
    } catch (Exception e) {
      log.error("Failed to register type handlers", e);
      throw new RuntimeException("TypeHandler registration failed", e);
    }
  }

  private void registerJsonTypeHandlers(TypeHandlerRegistry registry) {
    log.debug("Registering json type handlers");

    registry.register(ConcreteElementA.class, JdbcType.VARCHAR, new ObjectJsonTypeHandler<>(ConcreteElementA.class));

    log.info("Successfully registered json type handlers");
  }

  private void registerArrayTypeHandlers(TypeHandlerRegistry registry) {
    log.debug("Registering array type handlers");

    registry.register(new IntegerArrayTypeHandler());
    registry.register(new LongArrayTypeHandler());
    registry.register(new BigDecimalArrayTypeHandler());
    registry.register(new StringArrayTypeHandler());

    log.info("Successfully registered array type handlers");
  }


}
