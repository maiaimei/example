package org.example.config;

import java.util.List;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.example.type.ListGenericTypeHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement       // 启用事务管理
@MapperScan("org.example.repository")  // 配置Mapper接口扫描路径
public class MyBatisConfig {

  @Bean
  public ConfigurationCustomizer configurationCustomizer() {
    return configuration -> {
      final TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

      typeHandlerRegistry.register(List.class, new ListGenericTypeHandler<>(String.class));
    };
  }
}
