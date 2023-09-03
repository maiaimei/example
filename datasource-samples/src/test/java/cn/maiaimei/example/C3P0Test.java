package cn.maiaimei.example;

import cn.maiaimei.example.util.C3P0Utils;
import java.sql.Connection;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class C3P0Test extends AbstractDBTest {

  @Override
  protected Connection getConnection() {
    return C3P0Utils.getConnection();
  }

  @AfterAll
  public static void tearDown() {
    C3P0Utils.closeConnection();
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
