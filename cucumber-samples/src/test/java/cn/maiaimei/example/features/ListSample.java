package cn.maiaimei.example.features;

import io.cucumber.java.Transpose;
import io.cucumber.java.en.Given;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListSample {

  @Given("[直列表] delete account")
  public void deleteAccount1(List<String> usernames) {
    log.info("execute deleteAccount1(), fields:[ usernames = {} ]", usernames);
  }

  /**
   * {@link Transpose}，告诉 Cucumber 需要进行数据转换
   */
  @Given("[横列表] delete account")
  public void deleteAccount2(@Transpose List<String> usernames) {
    log.info("execute deleteAccount2(), fields:[ usernames = {} ]", usernames);
  }
}
