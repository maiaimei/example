package cn.maiaimei.example;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBUtils {

  public static Connection getConnection() {
    try (HikariDataSource dataSource = new HikariDataSource()) {
      return dataSource.getConnection();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
