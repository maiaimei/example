package org.example.config;

import javax.sql.DataSource;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
//@org.springframework.context.annotation.Configuration
public class MultipleMyBatisConfig {

  @Primary
  @Bean
  public SqlSessionFactory primarySqlSessionFactory(
      @Qualifier("primaryDataSource") DataSource dataSource) throws Exception {
    return createSqlSessionFactory(dataSource, "org.example.primary.mapper");
  }

  @Bean
  public SqlSessionFactory secondarySqlSessionFactory(
      @Qualifier("secondaryDataSource") DataSource dataSource) throws Exception {
    return createSqlSessionFactory(dataSource, "org.example.secondary.mapper");
  }

  private SqlSessionFactory createSqlSessionFactory(
      DataSource dataSource, String mapperPackage) throws Exception {
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource);

    // MyBatis配置
    Configuration configuration = new Configuration();
    configuration.setMapUnderscoreToCamelCase(true);  // 开启驼峰命名转换
    configuration.setLogImpl(Slf4jImpl.class);        // 设置日志实现
    configuration.setCacheEnabled(true);              // 开启二级缓存
    configuration.setLazyLoadingEnabled(true);        // 开启懒加载
    configuration.setAggressiveLazyLoading(false);    // 关闭积极加载
    factoryBean.setConfiguration(configuration);

    // 设置mapper位置
    factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
        .getResources("classpath:mapper/" + mapperPackage.substring(mapperPackage.lastIndexOf(".") + 1) + "/*.xml"));

    return factoryBean.getObject();
  }

  @Primary
  @Bean
  public SqlSessionTemplate primarySqlSessionTemplate(
      @Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
    return new SqlSessionTemplate(sqlSessionFactory);
  }

  @Bean
  public SqlSessionTemplate secondarySqlSessionTemplate(
      @Qualifier("secondarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
    return new SqlSessionTemplate(sqlSessionFactory);
  }
}
