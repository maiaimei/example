package org.example.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.autoconfigure.DataSourceProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "spring.datasources.enabled", havingValue = "true", matchIfMissing = false)
public class MultipleDataSourceConfig {

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
