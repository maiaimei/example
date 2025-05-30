package org.example.dataSource.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.example.dataSource.DataSourceProperties;
import org.example.dataSource.SimpleDataSourceProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SimpleDataSourceProperties.class)
@ConditionalOnProperty(name = "spring.datasource.enabled", havingValue = "true", matchIfMissing = false)
public class DataSourceConfig {

  @Bean
  @ConditionalOnProperty(name = "spring.datasource.hikari.enabled", havingValue = "true", matchIfMissing = false)
  public DataSource hikariDataSource(SimpleDataSourceProperties properties) {
    HikariConfig config = new HikariConfig();

    // 设置数据库连接信息
    config.setJdbcUrl(properties.getUrl());
    config.setUsername(properties.getUsername());
    config.setPassword(properties.getPassword());
    config.setDriverClassName(properties.getDriverClassName());

    // 设置连接池配置
    DataSourceProperties.Pool pool = properties.getPool();
    config.setPoolName(pool.getPoolName());
    config.setMaximumPoolSize(pool.getMaximumPoolSize());
    config.setMinimumIdle(pool.getMinimumPoolSize());
    config.setConnectionTimeout(pool.getConnectionTimeoutMs());
    config.setIdleTimeout(pool.getConnectionIdleTimeoutMs());

    return new HikariDataSource(config);
  }

  @Bean
  @ConditionalOnProperty(name = "spring.datasource.druid.enabled", havingValue = "true", matchIfMissing = false)
  public DataSource druidDataSource(SimpleDataSourceProperties properties) {
    // 使用Druid数据源
    DruidDataSource druidDataSource = new DruidDataSource();

    // 设置数据库连接信息
    druidDataSource.setUrl(properties.getUrl());
    druidDataSource.setUsername(properties.getUsername());
    druidDataSource.setPassword(properties.getPassword());
    druidDataSource.setDriverClassName(properties.getDriverClassName());

    // 设置连接池配置
    DataSourceProperties.Pool pool = properties.getPool();
    druidDataSource.setName(pool.getPoolName());
    druidDataSource.setInitialSize(pool.getInitialPoolSize());
    druidDataSource.setMinIdle(pool.getMinimumPoolSize());
    druidDataSource.setMaxActive(pool.getMaximumPoolSize());
    druidDataSource.setMaxWait(pool.getConnectionTimeoutMs());
    druidDataSource.setTimeBetweenEvictionRunsMillis(pool.getIdleConnectionCheckIntervalMs());
    druidDataSource.setMinEvictableIdleTimeMillis(pool.getConnectionIdleTimeoutMs());

    return druidDataSource;
  }
}
