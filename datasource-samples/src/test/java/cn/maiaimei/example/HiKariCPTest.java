package cn.maiaimei.example;

import cn.maiaimei.example.config.TestDBConfig;
import cn.maiaimei.example.util.HiKariCPUtils;
import com.zaxxer.hikari.HikariConfig;
import java.sql.Connection;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class HiKariCPTest extends AbstractDBCPTest {

  String key = "testdb";

  @Override
  protected Connection getConnection() {
    return HiKariCPUtils.getConnection(key);
  }

  @BeforeEach
  public void setUp() {
    HiKariCPUtils.createDataSource(key, getHikariConfig(key));
  }

  @AfterEach
  public void tearDown() {
    HiKariCPUtils.closeConnection(key);
  }

  @Test
  public void test_list() {
    log.info("-------------------1st list---------------------");
    list();
    log.info("-------------------2nd list-------------------");
    list();
    log.info("-------------------3rd list-------------------");
    list();
  }

  @Test
  public void test_get() {
    get(1594223173233131520L);
    get(1594223173619007488L);
  }

  @Test
  public void test_insert() {
    insert();
    insert();
    insert();
  }

  @Test
  public void test_update() {
    update(1698195993469784064L);
    update(1698202322368335872L);
    update(1698202322552885248L);
  }

  @Test
  public void test_delete() {
    delete(1698195993469784064L);
    delete(1698202322368335872L);
    delete(1698202322552885248L);
  }

  private HikariConfig getHikariConfig(String key) {
    HikariConfig config = new HikariConfig();
    config.setDriverClassName(TestDBConfig.driverClassName);
    config.setJdbcUrl(TestDBConfig.url);
    config.setUsername(TestDBConfig.username);
    config.setPassword(TestDBConfig.password);
    config.setPoolName("HikariPool-" + key);
    config.setMaximumPoolSize(1);
    config.setMinimumIdle(1);
    config.setMaxLifetime(1800000);
    config.setIdleTimeout(600000);
    config.setConnectionTimeout(30000);
    config.setAutoCommit(true);
    return config;
  }
}
