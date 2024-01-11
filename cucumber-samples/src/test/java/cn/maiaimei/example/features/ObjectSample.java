package cn.maiaimei.example.features;

import cn.maiaimei.example.model.Account;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectSample {

  @Given("[对象化] add account")
  public void addAccount(List<Account> accountList) {
    for (Account account : accountList) {
      log.info("execute addAccount(), fields:[ account = {} ]", account);
    }
  }

  /**
   * 帐号对象 的 封装方法
   * <p>在封装方法上方，需要加上 @DataTableType 注解</p>
   * <p>方法参数中，直接指定 对象封装方法 返回的 对象类型，Cucumber 就能直接进行关联</p>
   */
  @DataTableType
  public Account defineAccount(Map<String, String> entry) {
    Account account = new Account();
    // 如果有指定数据，则将数据写入  
    Optional.ofNullable(entry.get("username")).ifPresent(account::setUsername);
    Optional.ofNullable(entry.get("role")).ifPresent(account::setRole);
    Optional.ofNullable(entry.get("project")).ifPresent(account::setProject);
    return account;
  }

}
