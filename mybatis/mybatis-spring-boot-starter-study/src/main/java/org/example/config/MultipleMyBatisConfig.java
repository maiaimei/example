package org.example.config;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class MultipleMyBatisConfig extends AbstractMyBatisConfig {

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

    factoryBean.setConfiguration(getConfiguration());

    // 设置mapper位置
//    factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
//        .getResources("classpath:mapper/" + mapperPackage.substring(mapperPackage.lastIndexOf(".") + 1) + "/*.xml"));

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
