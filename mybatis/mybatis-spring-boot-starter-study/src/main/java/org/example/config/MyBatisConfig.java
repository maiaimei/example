package org.example.config;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement // 启用注解事务管理
@MapperScan("org.example.repository")  // 指定Mapper接口包路径
public class MyBatisConfig extends AbstractMyBatisConfig {

  /**
   * 配置SqlSessionFactory
   */
  @Bean
  public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource);

    factoryBean.setConfiguration(getConfiguration());

    // 设置类型别名包
    factoryBean.setTypeAliasesPackage("com.example.domain");

    // 设置mapper.xml文件位置
//    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//    factoryBean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));

    factoryBean.setPlugins(getPageInterceptor("mysql"));

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
