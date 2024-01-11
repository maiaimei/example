package cn.maiaimei.example.features;

import io.cucumber.java.en.But;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BackgroundSample {

  @Given("[Background Sample] add account {string}")
  public void addAccount(String account) {
    log.info("[Background Sample] add account {}", account);
  }

  @Given("[Background Sample] add role {string}, binding permissions {string}")
  public void addRole(String role, String permissions) {
    log.info("[Background Sample] add role {}, binding permissions {}", role, permissions);
  }

  @When("[Background Sample] account {string} binding role {string}")
  public void accountBindingRole(String account, String role) {
    log.info("[Background Sample] account {} binding role {}", account, role);
  }

  @Then("[Background Sample] authentication account {string}, with permission {string}")
  public void authenticationAccountWithPermission(String account, String permission) {
    log.info("[Background Sample] authentication account {}, with permission {}", account,
        permission);
  }

  @But("[Background Sample] authentication account {string}, without permission {string}")
  public void authenticationAccountWithoutPermission(String account, String permission) {
    log.info("[Background Sample] authentication account {}, without permission {}", account,
        permission);
  }

  @Given("[Background Sample] add organization {string}, binding permissions {string}")
  public void addOrganization(String organization, String permissions) {
    log.info("[Background Sample] add organization {}, binding permissions {}", organization,
        permissions);
  }

  @When("[Background Sample] account {string} binding organization {string}")
  public void accountBindingOrganization(String account, String organization) {
    log.info("[Background Sample] account {} binding organization {}", account, organization);
  }
}
