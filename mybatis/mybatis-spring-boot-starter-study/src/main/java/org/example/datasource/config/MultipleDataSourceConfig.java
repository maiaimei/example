package org.example.datasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;
import org.example.datasource.DynamicRoutingDataSource;
import org.example.datasource.autoconfigure.DataSourceProperties;
import org.example.datasource.autoconfigure.MultipleDataSourceProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

@Configuration
@EnableConfigurationProperties(MultipleDataSourceProperties.class)
@ConditionalOnProperty(name = "spring.datasources.enabled", havingValue = "true", matchIfMissing = false)
public class MultipleDataSourceConfig {

  @Bean("masterDataSource")
  @ConditionalOnProperty(name = "spring.datasources.hikari.enabled", havingValue = "true", matchIfMissing = false)
  public DataSource masterHikariDataSource(MultipleDataSourceProperties properties) {
    final DataSourceProperties dataSourceProperties = getDataSourceProperties("master", properties);
    return createHikariDataSource(dataSourceProperties);
  }

  @Bean("slave1DataSource")
  @ConditionalOnProperty(name = "spring.datasources.hikari.enabled", havingValue = "true", matchIfMissing = false)
  public DataSource slave1HikariDataSource(MultipleDataSourceProperties properties) {
    final DataSourceProperties dataSourceProperties = getDataSourceProperties("slave1", properties);
    return createHikariDataSource(dataSourceProperties);
  }

  @Bean("masterDataSource")
  @ConditionalOnProperty(name = "spring.datasources.druid.enabled", havingValue = "true", matchIfMissing = false)
  public DataSource masterDruidDataSource(MultipleDataSourceProperties properties) {
    final DataSourceProperties dataSourceProperties = getDataSourceProperties("master", properties);
    return createDruidDataSource(dataSourceProperties);
  }

  @Bean("slave1DataSource")
  @ConditionalOnProperty(name = "spring.datasources.druid.enabled", havingValue = "true", matchIfMissing = false)
  public DataSource slave1DruidDataSource(MultipleDataSourceProperties properties) {
    final DataSourceProperties dataSourceProperties = getDataSourceProperties("slave1", properties);
    return createDruidDataSource(dataSourceProperties);
  }

  @Bean
  public DataSource routingDataSource(DataSource masterDataSource, DataSource slave1DataSource) {
    DynamicRoutingDataSource dynamicDataSource = new DynamicRoutingDataSource();

    // 配置数据源
    Map<Object, Object> dataSourceMap = new HashMap<>(2);
    dataSourceMap.put("master", masterDataSource);
    dataSourceMap.put("slave1", slave1DataSource);

    // 设置数据源映射
    dynamicDataSource.setTargetDataSources(dataSourceMap);

    // 设置默认数据源
    dynamicDataSource.setDefaultTargetDataSource(masterDataSource);

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

  private DataSourceProperties getDataSourceProperties(String dataSourceName, MultipleDataSourceProperties properties) {
    if (Objects.isNull(properties) || CollectionUtils.isEmpty(properties.getShard())) {
      throw new IllegalArgumentException("Invalid data source");
    }
    final Optional<DataSourceProperties> optional = properties.getShard().stream()
        .filter(prop -> dataSourceName.equals(prop.getName())).findFirst();
    if (optional.isPresent()) {
      return optional.get();
    }
    throw new IllegalArgumentException("No data source found, name: " + dataSourceName);
  }
}
