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
import org.springframework.context.annotation.Primary;
import org.springframework.util.CollectionUtils;

@Configuration
@EnableConfigurationProperties(MultipleDataSourceProperties.class)
@ConditionalOnProperty(name = "spring.datasources.enabled", havingValue = "true", matchIfMissing = false)
public class MultipleDataSourceConfig {

  @Bean("userCenterDataSource")
  @ConditionalOnProperty(name = "spring.datasources.hikari.enabled", havingValue = "true", matchIfMissing = false)
  public DataSource userCenterHikariDataSource(MultipleDataSourceProperties properties) {
    final DataSourceProperties dataSourceProperties = getDataSourceProperties("userCenter", properties);
    return createHikariDataSource(dataSourceProperties);
  }

  @Bean("productCenterDataSource")
  @ConditionalOnProperty(name = "spring.datasources.hikari.enabled", havingValue = "true", matchIfMissing = false)
  public DataSource productCenterHikariDataSource(MultipleDataSourceProperties properties) {
    final DataSourceProperties dataSourceProperties = getDataSourceProperties("productCenter", properties);
    return createHikariDataSource(dataSourceProperties);
  }

  @Bean("userCenterDataSource")
  @ConditionalOnProperty(name = "spring.datasources.druid.enabled", havingValue = "true", matchIfMissing = false)
  public DataSource userCenterDruidDataSource(MultipleDataSourceProperties properties) {
    final DataSourceProperties dataSourceProperties = getDataSourceProperties("userCenter", properties);
    return createDruidDataSource(dataSourceProperties);
  }

  @Bean("productCenterDataSource")
  @ConditionalOnProperty(name = "spring.datasources.druid.enabled", havingValue = "true", matchIfMissing = false)
  public DataSource productCenterDruidDataSource(MultipleDataSourceProperties properties) {
    final DataSourceProperties dataSourceProperties = getDataSourceProperties("productCenter", properties);
    return createDruidDataSource(dataSourceProperties);
  }

  @Primary
  @Bean
  public DataSource dynamicDataSource(DataSource userCenterDataSource, DataSource productCenterDataSource) {
    DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();

    // 配置数据源
    Map<Object, Object> dataSourceMap = new HashMap<>(2);
    dataSourceMap.put("userCenter", userCenterDataSource);
    dataSourceMap.put("productCenter", productCenterDataSource);

    // 设置数据源映射
    dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);

    // 设置默认数据源
    dynamicRoutingDataSource.setDefaultTargetDataSource(userCenterDataSource);

    return dynamicRoutingDataSource;
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
