package org.example.mybatis.config;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan(value = {
    "org.example.repository.master",
    "org.example.repository.slave1"
})
@ConditionalOnProperty(name = "spring.datasources.enabled", havingValue = "true", matchIfMissing = false)
public class MultipleMyBatisConfig extends AbstractMyBatisConfig {

  @Primary
  @Bean
  public SqlSessionFactory masterSqlSessionFactory(
      @Qualifier("masterDataSource") DataSource dataSource) throws Exception {
    return createSqlSessionFactory(dataSource, "com.example.domain", "org.example.repository.master");
  }

  @Bean
  public SqlSessionFactory slave1SqlSessionFactory(
      @Qualifier("slave1DataSource") DataSource dataSource) throws Exception {
    return createSqlSessionFactory(dataSource, "com.example.domain", "org.example.repository.slave1");
  }

  private SqlSessionFactory createSqlSessionFactory(DataSource dataSource,
      String entityPackage,
      String mapperPackage) throws Exception {
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource);

    factoryBean.setConfiguration(getConfiguration());

    // 设置类型别名包
    factoryBean.setTypeAliasesPackage(entityPackage); // 相当于 yaml 配置的 mybatis.type-aliases-package

    // 设置mapper位置
//    factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
//        .getResources("classpath:mapper/" + mapperPackage.substring(mapperPackage.lastIndexOf(".") + 1) + "/*.xml"));

    return factoryBean.getObject();
  }

  @Primary
  @Bean
  public SqlSessionTemplate masterSqlSessionTemplate(
      @Qualifier("masterSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
    return new SqlSessionTemplate(sqlSessionFactory);
  }

  @Bean
  public SqlSessionTemplate slave1SqlSessionTemplate(
      @Qualifier("slave1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
    return new SqlSessionTemplate(sqlSessionFactory);
  }

  /**
   * 配置事务管理器
   */
  @Primary
  @Bean
  public DataSourceTransactionManager masterTransactionManager(@Qualifier("masterDataSource") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  @Bean
  public DataSourceTransactionManager slave1TransactionManager(@Qualifier("slave1DataSource") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

}
