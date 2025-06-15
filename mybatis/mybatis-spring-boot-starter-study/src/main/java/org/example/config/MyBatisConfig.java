package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.example.model.domain.Person;
import org.example.model.domain.Role;
import org.example.mybatis.interceptor.ConditionInterceptor;
import org.example.mybatis.interceptor.TypeHandlerInterceptor;
import org.example.mybatis.type.CustomTypeHandlerRegistry;
import org.example.type.*;
import org.mybatis.spring.SqlSessionFactoryBean;
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

      // 1. 先注册具体类型的JSON处理器（最具体的类型）
      registerJsonTypeHandlers(registry);

      // 2. 注册数组类型处理器（次具体的类型）
      registerArrayTypeHandlers(registry);

      // 3. 注册简单集合类型处理器
      registerSimpleCollectionTypeHandlers(registry);

      // 4. 最后注册Map相关的处理器（最抽象的类型）
      registerJsonMapTypeHandlers(registry);

      log.info("Successfully registered all type handlers");
    } catch (Exception e) {
      log.error("Failed to register type handlers", e);
      throw new RuntimeException("TypeHandler registration failed", e);
    }
  }

  private void registerJsonTypeHandlers(TypeHandlerRegistry registry) {
    log.debug("Registering json type handlers");

    // 注册具体类型的JSON处理器
    registry.register(Person.class, JdbcType.OTHER,
        new JsonTypeHandler<>(Person.class, objectMapper));
    registry.register(Role.class, JdbcType.OTHER,
        new JsonTypeHandler<>(Role.class, objectMapper));

    // 注册具体类型的List JSON处理器
    registry.register(List.class, JdbcType.OTHER,
        new JsonListTypeHandler<>(Person.class, objectMapper));
  }

  private void registerArrayTypeHandlers(TypeHandlerRegistry registry) {
    log.debug("Registering array type handlers");

    // 注册数组类型处理器
    registry.register(String[].class, new StringArrayTypeHandler());
    registry.register(Integer[].class, new IntegerArrayTypeHandler());
    registry.register(BigDecimal[].class, new BigDecimalArrayTypeHandler());
    registry.register(Long[].class, new LongArrayTypeHandler());
  }

  private void registerSimpleCollectionTypeHandlers(TypeHandlerRegistry registry) {
    log.debug("Registering simple collection type handlers");

    // 注册基础类型的List处理器
    registry.register(new StringListTypeHandler());
    registry.register(new IntegerListTypeHandler());
    registry.register(new BigDecimalListTypeHandler());
    registry.register(new LongListTypeHandler());
  }

  private void registerJsonMapTypeHandlers(TypeHandlerRegistry registry) {
    log.debug("Registering json map type handlers");

    // 注册Map的JSON处理器
    registry.register(Map.class, JdbcType.OTHER,
        new JsonMapTypeHandler(objectMapper));

    // 注册List<Map>的JSON处理器
//    registry.register(List.class, JdbcType.OTHER,
//        new JsonMapListTypeHandler(objectMapper));
    registry.register(new JsonMapListTypeHandler(objectMapper));
  }

  /**
   * 自定义TypeHandler注册表
   */
  //@Bean
  public CustomTypeHandlerRegistry customTypeHandlerRegistry() {
    CustomTypeHandlerRegistry registry = new CustomTypeHandlerRegistry();
    // 扫描实体类包
    registry.scanAndRegisterTypeHandlers("org.example.model.domain");
    return registry;
  }

  /**
   * TypeHandler拦截器
   */
  //@Bean
  public TypeHandlerInterceptor typeHandlerInterceptor(CustomTypeHandlerRegistry customTypeHandlerRegistry) {
    return new TypeHandlerInterceptor(customTypeHandlerRegistry);
  }

  /**
   * 配置SqlSessionFactory
   */
  //@Bean
  public SqlSessionFactory sqlSessionFactory(DataSource dataSource,
      CustomTypeHandlerRegistry customTypeHandlerRegistry,
      TypeHandlerInterceptor typeHandlerInterceptor) throws Exception {
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource);

    // 配置MyBatis
    org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
    // 注册TypeHandler
    registerTypeHandlers(configuration.getTypeHandlerRegistry(), customTypeHandlerRegistry);
    // 添加拦截器
    configuration.addInterceptor(typeHandlerInterceptor);

    factoryBean.setConfiguration(configuration);
    return factoryBean.getObject();
  }

  private void registerTypeHandlers(TypeHandlerRegistry mybatisRegistry,
      CustomTypeHandlerRegistry customRegistry) {
    customRegistry.getTypeHandlers().forEach(mybatisRegistry::register);
  }


}
