package cn.maiaimei.example.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * https://zhuanlan.zhihu.com/p/654041007
 */
public class ShoppingSteps {

  @Given("the following groceries:")
  public void theFollowingGroceries() {
  }

  @When("I pay {int}")
  public void iPay(int arg0) {

  }

  @Then("my change should be {int}")
  public void myChangeShouldBe(int arg0) {
  }
}
