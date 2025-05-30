package org.example.dataSource.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.example.dataSource.DataSourceProperties;
import org.example.dataSource.DynamicRoutingDataSource;
import org.example.dataSource.MultipleDataSourceProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MultipleDataSourceProperties.class)
@ConditionalOnProperty(name = "spring.datasources.enabled", havingValue = "true", matchIfMissing = false)
public class MultipleDataSourceConfig {

  @Bean
  @ConditionalOnProperty(name = "spring.datasources.hikari.enabled", havingValue = "true", matchIfMissing = false)
  public DataSource routingHikariDataSource(MultipleDataSourceProperties properties) {
    Map<Object, Object> targetDataSources = new HashMap<>();

    // 创建所有配置的数据源
    properties.getShard().forEach(prop -> {
      DataSource ds = createHikariDataSource(prop);
      targetDataSources.put(prop.getName(), ds);
    });

    // 创建动态数据源
    DynamicRoutingDataSource dynamicDataSource = new DynamicRoutingDataSource();
    dynamicDataSource.setTargetDataSources(targetDataSources);
    dynamicDataSource.setDefaultTargetDataSource(targetDataSources.get("master"));

    return dynamicDataSource;
  }

  @Bean
  @ConditionalOnProperty(name = "spring.datasources.druid.enabled", havingValue = "true", matchIfMissing = false)
  public DataSource routingDruidDataSource(MultipleDataSourceProperties properties) {
    Map<Object, Object> targetDataSources = new HashMap<>();

    // 创建所有配置的数据源
    properties.getShard().forEach(prop -> {
      DataSource ds = createDruidDataSource(prop);
      targetDataSources.put(prop.getName(), ds);
    });

    // 创建动态数据源
    DynamicRoutingDataSource dynamicDataSource = new DynamicRoutingDataSource();
    dynamicDataSource.setTargetDataSources(targetDataSources);
    dynamicDataSource.setDefaultTargetDataSource(targetDataSources.get("master"));

    return dynamicDataSource;
  }

  private HikariDataSource createHikariDataSource(DataSourceProperties properties) {
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

  private DruidDataSource createDruidDataSource(DataSourceProperties properties) {
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
