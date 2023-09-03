package cn.maiaimei.example;

import cn.maiaimei.example.model.DBProperties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JdbcTest extends AbstractDBTest {

  private static final DBProperties dbProperties = DBProperties.newInstance();

  @Override
  protected Connection getConnection() {
    // 2.获取连接
    try {
      return DriverManager.getConnection(
          dbProperties.getUrl(),
          dbProperties.getUsername(),
          dbProperties.getPassword()
      );
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @BeforeAll
  public static void setUp() {
    // 1.注册驱动
    try {
      Class.forName(dbProperties.getDriverClassName());
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Order(4)
  @Test
  public void test_list() {
    for (int i = 1; i <= 3; i++) {
      log.info("===> List({}) records start", i);
      list();
      log.info("<=== List({}) records end", i);
    }
  }

  @Order(3)
  @Test
  public void test_get() {
    get(1698195993469784064L);
    get(1698202322368335872L);
    get(1698202322552885248L);
  }

  @Order(1)
  @Test
  public void test_insert() {
    insert(1698195993469784064L);
    insert(1698202322368335872L);
    insert(1698202322552885248L);
  }

  @Order(2)
  @Test
  public void test_update() {
    update(1698195993469784064L);
    update(1698202322368335872L);
    update(1698202322552885248L);
  }

  @Order(5)
  @Test
  public void test_delete() {
    delete(1698195993469784064L);
    delete(1698202322368335872L);
    delete(1698202322552885248L);
  }
}
