package cn.maiaimei.example.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class C3P0Utils {

  private static final ComboPooledDataSource DATA_SOURCE;

  static {
    // 读取c3p0.properties配置
    DATA_SOURCE = new ComboPooledDataSource();
  }

  private C3P0Utils() {
    throw new UnsupportedOperationException();
  }

  public static Connection getConnection() {
    try {
      return DATA_SOURCE.getConnection();
    } catch (SQLException e) {
      log.error("Can not get a connection", e);
      throw new RuntimeException(e);
    }
  }

  public static void closeConnection() {
    log.info("Try to release connection");
    DATA_SOURCE.close();
    log.info("Release connection completed");
  }
}
