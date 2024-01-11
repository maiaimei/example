package cn.maiaimei.example.features;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScenarioOutlineSample {

  @Given("[Scenario Outline Sample] add role {} binding permissions {}")
  public void addRoleBindingPermissions(String role, String permissions) {
    log.info("add role {} binding permissions {}", role, permissions);
  }

  @When("[Scenario Outline Sample] add account {}")
  public void addAccount(String account) {
    log.info("add account {}", account);
  }

  @And("[Scenario Outline Sample] account {} binding role {}")
  public void accountBindingRole(String account, String role) {
    log.info("account {} binding role {}", account, role);
  }

  @Then("[Scenario Outline Sample] authentication account {}, with permission {}")
  public void authenticationAccountWithPermission(String account, String has_permission) {
    log.info("authentication account {}, with permission {}", account, has_permission);
  }
}
