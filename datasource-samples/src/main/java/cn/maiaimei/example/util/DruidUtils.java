package cn.maiaimei.example.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DruidUtils {

  private static final DataSource DATA_SOURCE;

  static {
    Properties properties = new Properties();
    InputStream is = DruidUtils.class.getClassLoader().getResourceAsStream("druid.properties");
    try {
      properties.load(is);
      DATA_SOURCE = DruidDataSourceFactory.createDataSource(properties);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private DruidUtils() {
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
    ((DruidDataSource) DATA_SOURCE).close();
    log.info("Release connection completed");
  }
}
