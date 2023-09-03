package cn.maiaimei.example.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

@Slf4j
public final class HiKariCPUtils {

  private static final Map<String, HikariDataSource> DATA_SOURCE_MAP = new ConcurrentHashMap<>();

  private HiKariCPUtils() {
    throw new UnsupportedOperationException();
  }

  public synchronized static void createDataSource(String key, HikariConfig config) {
    DATA_SOURCE_MAP.put(key, getHikariDataSource(config));
  }

  public static Connection getConnection(String key) {
    final HikariDataSource dataSource = DATA_SOURCE_MAP.get(key);
    if (CollectionUtils.isEmpty(DATA_SOURCE_MAP) || Objects.isNull(dataSource)) {
      String message = "Can not get a connection named " + key;
      log.error(message);
      throw new RuntimeException(message);
    }
    try {
      return dataSource.getConnection();
    } catch (SQLException e) {
      log.error(String.format("Can not get a connection named %s", key), e);
      throw new RuntimeException(e);
    }
  }

  public synchronized static void closeConnection(String key) {
    if (!CollectionUtils.isEmpty(DATA_SOURCE_MAP)) {
      final HikariDataSource dataSource = DATA_SOURCE_MAP.remove(key);
      if (Objects.nonNull(dataSource)) {
        log.info("Try to release connection for key: {}", key);
        dataSource.close();
      }
    }
  }

  private static HikariDataSource getHikariDataSource(HikariConfig config) {
    log.info("Try to establish connection to url: {}", config.getJdbcUrl());
    return new HikariDataSource(config);
  }
}
