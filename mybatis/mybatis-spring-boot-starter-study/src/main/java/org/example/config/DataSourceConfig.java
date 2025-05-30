package org.example.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.example.autoconfigure.DataSourceProperties;
import org.example.autoconfigure.SimpleDataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SimpleDataSourceProperties.class)
public class DataSourceConfig {

  @Bean
  public DataSource hikariDataSource(SimpleDataSourceProperties properties) {
    HikariConfig config = new HikariConfig();

    // 设置数据库连接信息
    config.setJdbcUrl(properties.getUrl());
    config.setUsername(properties.getUsername());
    config.setPassword(properties.getPassword());
    config.setDriverClassName(properties.getDriverClassName());

    // 设置连接池配置
    DataSourceProperties.Pool pool = properties.getPool();
    config.setMaximumPoolSize(pool.getMaxActive());
    config.setMinimumIdle(pool.getMinIdle());
    config.setConnectionTimeout(pool.getMaxWait());
    config.setIdleTimeout(pool.getMinEvictableIdleTimeMillis());

    return new HikariDataSource(config);
  }

  //@Bean
  public DataSource druidDataSource(SimpleDataSourceProperties properties) {
    // 使用Druid数据源
    DruidDataSource dataSource = new DruidDataSource();

    // 设置数据库连接信息
    dataSource.setUrl(properties.getUrl());
    dataSource.setUsername(properties.getUsername());
    dataSource.setPassword(properties.getPassword());
    dataSource.setDriverClassName(properties.getDriverClassName());

    // 设置连接池配置
    DataSourceProperties.Pool pool = properties.getPool();
    dataSource.setInitialSize(pool.getInitialSize());
    dataSource.setMinIdle(pool.getMinIdle());
    dataSource.setMaxActive(pool.getMaxActive());
    dataSource.setMaxWait(pool.getMaxWait());
    dataSource.setTimeBetweenEvictionRunsMillis(pool.getTimeBetweenEvictionRunsMillis());
    dataSource.setMinEvictableIdleTimeMillis(pool.getMinEvictableIdleTimeMillis());

    return dataSource;
  }
}
