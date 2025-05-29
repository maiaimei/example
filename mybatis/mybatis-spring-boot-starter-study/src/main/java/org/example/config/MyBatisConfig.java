package org.example.config;

import com.github.pagehelper.PageInterceptor;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("org.example.repository")  // 指定Mapper接口包路径
@EnableTransactionManagement // 启用注解事务管理
@org.springframework.context.annotation.Configuration
public class MyBatisConfig {

  /**
   * 配置SqlSessionFactory
   */
  @Bean
  public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource);

    // 设置MyBatis配置
    Configuration configuration = new Configuration();
    configuration.setMapUnderscoreToCamelCase(true);  // 开启驼峰命名转换
    configuration.setLogImpl(Slf4jImpl.class);        // 设置日志实现
    configuration.setCacheEnabled(true);              // 开启二级缓存
    configuration.setLazyLoadingEnabled(true);        // 开启懒加载
    configuration.setAggressiveLazyLoading(false);    // 关闭积极加载
    factoryBean.setConfiguration(configuration);

    // 设置类型别名包
    factoryBean.setTypeAliasesPackage("com.example.domain");

    // 设置mapper.xml文件位置
//    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//    factoryBean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));

    // 配置分页插件
    Properties properties = new Properties();
    properties.setProperty("helperDialect", "mysql");  // 指定数据库类型
    properties.setProperty("reasonable", "true");      // 启用合理化
    properties.setProperty("supportMethodsArguments", "true");
    properties.setProperty("params", "count=countSql");
    PageInterceptor pageInterceptor = new PageInterceptor();
    pageInterceptor.setProperties(properties);
    factoryBean.setPlugins(pageInterceptor);

    return factoryBean.getObject();
  }

  /**
   * 配置SqlSessionTemplate
   */
  @Bean
  public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
    return new SqlSessionTemplate(sqlSessionFactory);
  }

  /**
   * 配置事务管理器
   */
  @Bean
  public DataSourceTransactionManager transactionManager(DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

}
