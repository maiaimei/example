package org.example.mybatis.config;

import com.github.pagehelper.PageInterceptor;
import java.util.Properties;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.session.Configuration;

public abstract class AbstractMyBatisConfig {

  protected Configuration getConfiguration() {
    // 设置MyBatis配置
    Configuration configuration = new Configuration();
    configuration.setMapUnderscoreToCamelCase(true);  // 开启驼峰命名转换，相当于 yaml 配置的 mybatis.configuration.map-underscore-to-camel-case
    configuration.setLogImpl(Slf4jImpl.class);        // 设置日志实现，相当于 yaml 配置的 mybatis.configuration.log-impl
    configuration.setCacheEnabled(true);              // 开启二级缓存
    configuration.setLazyLoadingEnabled(true);        // 开启懒加载
    configuration.setAggressiveLazyLoading(false);    // 关闭积极加载

    // 处理 Boolean 类型映射
    //configuration.getTypeHandlerRegistry().register(Boolean.class, CustomBooleanTypeHandler.class);
    //configuration.getTypeHandlerRegistry().register(boolean.class, CustomBooleanTypeHandler.class);

    return configuration;
  }

  protected PageInterceptor getPageInterceptor(String databaseType) {
    // 配置分页插件
    Properties properties = new Properties();
    properties.setProperty("helperDialect", databaseType);  // 指定数据库类型
    properties.setProperty("reasonable", "true");      // 启用合理化
    properties.setProperty("supportMethodsArguments", "true");
    properties.setProperty("params", "count=countSql");
    PageInterceptor pageInterceptor = new PageInterceptor();
    pageInterceptor.setProperties(properties);
    return pageInterceptor;
  }
}
