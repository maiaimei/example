package org.example.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

  @Override
  protected Object determineCurrentLookupKey() {
    String dataSourceName = DataSourceContextHolder.getDataSourceName();
    log.info("Current DataSource: {}", dataSourceName);
    return dataSourceName;
  }
}
