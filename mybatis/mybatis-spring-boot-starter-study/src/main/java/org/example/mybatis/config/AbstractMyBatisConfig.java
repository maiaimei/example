package org.example.mybatis.config;

import com.fasterxml.jackson.core.type.TypeReference;
import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.example.mybatis.interceptor.ConditionInterceptor;
import org.example.mybatis.typehandler.BigDecimalArrayTypeHandler;
import org.example.mybatis.typehandler.BigDecimalListTypeHandler;
import org.example.mybatis.typehandler.BooleanTypeHandler;
import org.example.mybatis.typehandler.JsonTypeHandler;

public abstract class AbstractMyBatisConfig {

  protected Configuration getConfiguration() {
    Configuration configuration = new Configuration();
    configureSettings(configuration);
    registerTypeHandlers(configuration.getTypeHandlerRegistry());
    configuration.addInterceptor(new ConditionInterceptor());
    return configuration;
  }

  /**
   * 配置 MyBatis 基本设置
   */
  private void configureSettings(Configuration configuration) {
    configuration.setMapUnderscoreToCamelCase(true); // 开启驼峰命名转换
    configuration.setLogImpl(Slf4jImpl.class); // 设置日志实现
    configuration.setCacheEnabled(false); // 关闭二级缓存
    configuration.setLazyLoadingEnabled(false); // 关闭懒加载
    configuration.setAggressiveLazyLoading(false); // 关闭积极加载
  }

  /**
   * 注册自定义 TypeHandler
   */
  private void registerTypeHandlers(TypeHandlerRegistry typeHandlerRegistry) {
    // 注册 CustomBooleanTypeHandler
    registerBooleanTypeHandlers(typeHandlerRegistry);

    // 注册 BigDecimal 处理器
    typeHandlerRegistry.register(BigDecimal[].class, JdbcType.ARRAY, BigDecimalArrayTypeHandler.class);
    typeHandlerRegistry.register(List.class, JdbcType.ARRAY, BigDecimalListTypeHandler.class);

    // 注册通用的 JSON 处理器
    registerJsonTypeHandlers(typeHandlerRegistry);
  }

  /**
   * 注册 Boolean 类型处理器
   */
  private void registerBooleanTypeHandlers(TypeHandlerRegistry typeHandlerRegistry) {
    Class<?>[] booleanTypes = {Boolean.class, boolean.class};
    JdbcType[] jdbcTypes = {JdbcType.BOOLEAN, JdbcType.BIT, JdbcType.TINYINT, JdbcType.VARCHAR};

    for (Class<?> type : booleanTypes) {
      typeHandlerRegistry.register(type, BooleanTypeHandler.class);
      for (JdbcType jdbcType : jdbcTypes) {
        typeHandlerRegistry.register(type, jdbcType, BooleanTypeHandler.class);
      }
    }
  }

  /**
   * 注册 JSON 类型处理器
   */
  private void registerJsonTypeHandlers(TypeHandlerRegistry typeHandlerRegistry) {
    typeHandlerRegistry.register(List.class, JdbcType.VARCHAR, new JsonTypeHandler<>(new TypeReference<List<String>>() {
    }));
    typeHandlerRegistry.register(List.class, JdbcType.VARCHAR, new JsonTypeHandler<>(new TypeReference<List<Integer>>() {
    }));
    typeHandlerRegistry.register(List.class, JdbcType.VARCHAR, new JsonTypeHandler<>(new TypeReference<List<BigDecimal>>() {
    }));
  }

  // protected PageInterceptor getPageInterceptor(String databaseType) {
  // // 配置分页插件
  // Properties properties = new Properties();
  // properties.setProperty("helperDialect", databaseType); // 指定数据库类型
  // properties.setProperty("reasonable", "true"); // 启用合理化
  // properties.setProperty("supportMethodsArguments", "true");
  // properties.setProperty("params", "count=countSql");
  // PageInterceptor pageInterceptor = new PageInterceptor();
  // pageInterceptor.setProperties(properties);
  // return pageInterceptor;
  // }
}
