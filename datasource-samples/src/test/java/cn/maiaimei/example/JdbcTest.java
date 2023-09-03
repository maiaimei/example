package cn.maiaimei.example;

import cn.maiaimei.example.config.TestDBConfig;
import cn.maiaimei.example.model.User;
import cn.maiaimei.example.util.SFID;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class JdbcTest {

  @Test
  public void test_list() {
    String sql = "select * from sys_user";
    try {
      // 1.注册驱动
      Class.forName(TestDBConfig.driverClassName);
      // 6.使用 try-with-resources 自动释放资源
      try (
          // 2.获取连接
          final Connection conn = DriverManager.getConnection(
              TestDBConfig.url,
              TestDBConfig.username,
              TestDBConfig.password
          );
          // 3.获取数据库操作对象
          final PreparedStatement preparedStatement = conn.prepareStatement(sql);
          // 4.执行SQL语句
          final ResultSet resultSet = preparedStatement.executeQuery()
      ) {
        // 5.处理查询结果集
        while (resultSet.next()) {
          log.info("----------------------------------------");
          log.info("{}", resultSet.getLong(1));
          log.info("{}", resultSet.getString(2));
          log.info("{}", resultSet.getString(3));
          log.info("{}", resultSet.getString(4));
          log.info("{}", resultSet.getString("is_enabled"));
          log.info("{}", resultSet.getString("is_deleted"));
          log.info("{}", resultSet.getString("gmt_create"));
          log.info("{}", resultSet.getString("gmt_modified"));
        }
      }
    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void test_get() {
    String sql = "select * from sys_user where id = ?";
    try {
      // 1.注册驱动
      Class.forName(TestDBConfig.driverClassName);
      // 6.使用 try-with-resources 自动释放资源
      try (
          // 2.获取连接
          final Connection conn = DriverManager.getConnection(
              TestDBConfig.url,
              TestDBConfig.username,
              TestDBConfig.password
          );
          // 3.获取数据库操作对象
          final PreparedStatement preparedStatement = conn.prepareStatement(sql);
      ) {
        preparedStatement.setLong(1, 1594223173233131520L);
        try (
            // 4.执行SQL语句
            final ResultSet resultSet = preparedStatement.executeQuery()) {
          // 5.处理查询结果集
          while (resultSet.next()) {
            log.info("----------------------------------------");
            log.info("{}", resultSet.getLong(1));
            log.info("{}", resultSet.getString(2));
            log.info("{}", resultSet.getString(3));
            log.info("{}", resultSet.getString(4));
            log.info("{}", resultSet.getString("is_enabled"));
            log.info("{}", resultSet.getString("is_deleted"));
            log.info("{}", resultSet.getString("gmt_create"));
            log.info("{}", resultSet.getString("gmt_modified"));
          }
        }
      }
    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void test_insert() {
    final long id = SFID.nextId();
    final LocalDateTime now = LocalDateTime.now();
    final User user = User.builder()
        .id(id)
        .nickname(String.valueOf(id))
        .username(String.valueOf(id))
        .password(String.valueOf(id))
        .isEnabled(1)
        .isDeleted(0)
        .gmtCreate(now)
        .gmtModified(now)
        .build();
    String sql = "insert into sys_user (id, nickname, username, password, is_enabled, is_deleted,"
        + " gmt_create, gmt_modified) values (?, ?, ?, ?, ?, ?, ?, ?)";
    try {
      // 1.注册驱动
      Class.forName(TestDBConfig.driverClassName);
      // 5.使用 try-with-resources 自动释放资源
      try (
          // 2.获取连接
          final Connection conn = DriverManager.getConnection(
              TestDBConfig.url,
              TestDBConfig.username,
              TestDBConfig.password
          );
          // 3.获取数据库操作对象
          final PreparedStatement preparedStatement = conn.prepareStatement(sql)
      ) {
        preparedStatement.setLong(1, user.getId());
        preparedStatement.setString(2, user.getNickname());
        preparedStatement.setString(3, user.getUsername());
        preparedStatement.setString(4, user.getPassword());
        preparedStatement.setInt(5, user.getIsEnabled());
        preparedStatement.setInt(6, user.getIsDeleted());
        preparedStatement.setDate(7, java.sql.Date.valueOf(user.getGmtCreate().toLocalDate()));
        preparedStatement.setDate(8, java.sql.Date.valueOf(user.getGmtModified().toLocalDate()));
        // 4.执行SQL语句
        preparedStatement.executeUpdate();
      }
    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void test_update() {
    final long id = 1698186395509723136L;
    String sql = "update sys_user set is_enabled=?, gmt_modified=? where id=?";
    try {
      // 1.注册驱动
      Class.forName(TestDBConfig.driverClassName);
      // 5.使用 try-with-resources 自动释放资源
      try (
          // 2.获取连接
          final Connection conn = DriverManager.getConnection(
              TestDBConfig.url,
              TestDBConfig.username,
              TestDBConfig.password
          );
          // 3.获取数据库操作对象
          final PreparedStatement preparedStatement = conn.prepareStatement(sql)
      ) {
        preparedStatement.setLong(3, id);
        preparedStatement.setLong(1, 0);
        preparedStatement.setDate(2, java.sql.Date.valueOf(LocalDateTime.now().toLocalDate()));
        // 4.执行SQL语句
        preparedStatement.executeUpdate();
      }
    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void test_delete() {
    final long id = 1698186641841197056L;
    String sql = "delete from sys_user where id=?";
    try {
      // 1.注册驱动
      Class.forName(TestDBConfig.driverClassName);
      // 5.使用 try-with-resources 自动释放资源
      try (
          // 2.获取连接
          final Connection conn = DriverManager.getConnection(
              TestDBConfig.url,
              TestDBConfig.username,
              TestDBConfig.password
          );
          // 3.获取数据库操作对象
          final PreparedStatement preparedStatement = conn.prepareStatement(sql)
      ) {
        preparedStatement.setLong(1, id);
        // 4.执行SQL语句
        preparedStatement.executeUpdate();
      }
    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
