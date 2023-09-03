package cn.maiaimei.example;

import cn.maiaimei.example.util.C3P0Utils;
import java.sql.Connection;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

@Slf4j
public class C3P0Test extends AbstractDBCPTest {

  @Override
  protected Connection getConnection() {
    return C3P0Utils.getConnection();
  }

  @AfterAll
  public static void tearDown() {
    C3P0Utils.closeConnection();
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
    get(1698210186675752960L);
    get(1698210186663170048L);
    get(1698210185174192128L);
  }

  @Test
  public void test_insert() {
    insert();
    insert();
    insert();
  }

  @Test
  public void test_update() {
    update(1698210186675752960L);
    update(1698210186663170048L);
    update(1698210185174192128L);
  }

  @Test
  public void test_delete() {
    delete(1698210186675752960L);
    delete(1698210186663170048L);
    delete(1698210185174192128L);
  }
}
