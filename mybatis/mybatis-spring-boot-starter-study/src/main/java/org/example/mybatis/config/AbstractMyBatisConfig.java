package org.example.mybatis.config;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.example.mybatis.interceptor.ConditionInterceptor;
import org.example.mybatis.typehandler.BigDecimalArrayTypeHandler;
import org.example.mybatis.typehandler.CustomBooleanTypeHandler;

public abstract class AbstractMyBatisConfig {

  protected Configuration getConfiguration() {
    // 设置MyBatis配置
    Configuration configuration = new Configuration();
    configuration.setMapUnderscoreToCamelCase(true); // 开启驼峰命名转换，相当于 yaml 配置的
    // mybatis.configuration.map-underscore-to-camel-case
    configuration.setLogImpl(Slf4jImpl.class); // 设置日志实现，相当于 yaml 配置的 mybatis.configuration.log-impl
    configuration.setCacheEnabled(false); // 关闭二级缓存，禁用所有语句的缓存
    // configuration.setLocalCacheScope(LocalCacheScope.STATEMENT); // 禁用本地缓存
    configuration.setLazyLoadingEnabled(false); // 关闭懒加载
    configuration.setAggressiveLazyLoading(false); // 关闭积极加载

    final TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

    // 注册 CustomBooleanTypeHandler
    typeHandlerRegistry.register(Boolean.class, CustomBooleanTypeHandler.class);
    typeHandlerRegistry.register(Boolean.class, JdbcType.BOOLEAN, CustomBooleanTypeHandler.class);
    typeHandlerRegistry.register(Boolean.class, JdbcType.BIT, CustomBooleanTypeHandler.class);
    typeHandlerRegistry.register(Boolean.class, JdbcType.TINYINT, CustomBooleanTypeHandler.class);
    typeHandlerRegistry.register(Boolean.class, JdbcType.VARCHAR, CustomBooleanTypeHandler.class);
    typeHandlerRegistry.register(boolean.class, CustomBooleanTypeHandler.class);
    typeHandlerRegistry.register(boolean.class, JdbcType.BOOLEAN, CustomBooleanTypeHandler.class);
    typeHandlerRegistry.register(boolean.class, JdbcType.BIT, CustomBooleanTypeHandler.class);
    typeHandlerRegistry.register(boolean.class, JdbcType.TINYINT, CustomBooleanTypeHandler.class);
    typeHandlerRegistry.register(boolean.class, JdbcType.VARCHAR, CustomBooleanTypeHandler.class);

    // 注册 BigDecimalArrayTypeHandler
    typeHandlerRegistry.register(List.class, JdbcType.ARRAY, BigDecimalArrayTypeHandler.class);
    typeHandlerRegistry.register(BigDecimal[].class, JdbcType.ARRAY, BigDecimalArrayTypeHandler.class);

    configuration.addInterceptor(new ConditionInterceptor());

    return configuration;
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
