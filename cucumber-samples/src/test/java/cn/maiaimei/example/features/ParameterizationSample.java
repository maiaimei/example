package cn.maiaimei.example.features;

import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParameterizationSample {

  @Given("[Parameterization Sample] add role {string}, binding permissions {string}")
  public void addRole(String role, String permissions) {
    log.info("add role {}, binding permissions {}", role, permissions);
  }

  @And("[Parameterization Sample] add account {string}")
  public void addAccount(String account) {
    log.info("add account {}", account);
  }

  @When("[Parameterization Sample] account {string} binding role {string}")
  public void accountBindingRole(String account, String role) {
    log.info("account {} binding role {}", account, role);
  }

  @Then("[Parameterization Sample] authentication account {string}, with permission {string}")
  public void authenticationAccountWithPermission(String account, String permission) {
    log.info("authentication account {}, with permission {}", account, permission);
  }

  @But("[Parameterization Sample] authentication account {string}, without permission {string}")
  public void authenticationAccountWithoutPermission(String account, String permission) {
    log.info("authentication account {}, without permission {}", account, permission);
  }
}
