package org.example.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.example.type.ArrayTypeHandler;
import org.example.type.ListTypeHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement       // 启用事务管理
@MapperScan("org.example.mapper")  // 配置Mapper接口扫描路径
public class MybatisPlusConfig {

  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

    // 分页插件
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));

    // 防止全表更新与删除插件
    interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

    // 乐观锁插件
    interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

    return interceptor;
  }

  @Bean
  public ConfigurationCustomizer configurationCustomizer() {
    return configuration -> {
      // 注册类型处理器
      final TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

      typeHandlerRegistry.register(List.class, new ListTypeHandler<>(String.class));
      typeHandlerRegistry.register(List.class, new ListTypeHandler<>(Integer.class));
      typeHandlerRegistry.register(List.class, new ListTypeHandler<>(BigDecimal.class));

      typeHandlerRegistry.register(String[].class, new ArrayTypeHandler<>(String.class));
      typeHandlerRegistry.register(Integer[].class, new ArrayTypeHandler<>(Integer.class));
      typeHandlerRegistry.register(BigDecimal[].class, new ArrayTypeHandler<>(BigDecimal.class));

    };
  }
}