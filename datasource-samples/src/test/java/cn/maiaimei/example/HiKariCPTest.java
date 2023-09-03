package cn.maiaimei.example;

import cn.maiaimei.example.model.DBProperties;
import cn.maiaimei.example.util.HiKariCPUtils;
import com.zaxxer.hikari.HikariConfig;
import java.sql.Connection;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HiKariCPTest extends AbstractDBTest {

  private static final String key = "testdb";

  @Override
  protected Connection getConnection() {
    return HiKariCPUtils.getConnection(key);
  }

  @BeforeAll
  public static void beforeAll() {
    final DBProperties dbProperties = DBProperties.newInstance();
    HikariConfig config = new HikariConfig();
    config.setDriverClassName(dbProperties.getDriverClassName());
    config.setJdbcUrl(dbProperties.getUrl());
    config.setUsername(dbProperties.getUsername());
    config.setPassword(dbProperties.getPassword());
    config.setPoolName("HikariPool-" + key);
    config.setMaximumPoolSize(1);
    config.setMinimumIdle(1);
    config.setMaxLifetime(1800000);
    config.setIdleTimeout(600000);
    config.setConnectionTimeout(30000);
    config.setAutoCommit(true);
    HiKariCPUtils.createDataSource(key, config);
  }

  @AfterAll
  public static void afterAll() {
    HiKariCPUtils.closeConnection(key);
  }

  @Order(4)
  @Test
  public void test_list() {
    for (int i = 1; i <= 3; i++) {
      log.info("===> The {} time query start", i);
      list();
      log.info("===> The {} time query end", i);
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
