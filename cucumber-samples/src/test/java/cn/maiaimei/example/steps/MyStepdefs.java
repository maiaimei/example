package cn.maiaimei.example.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MyStepdefs {

  @Given("I have <opening balance> beer cans")
  public void iHaveOpeningBalanceBeerCans() {
  }

  @And("I have drunk {} beer cans")
  public void iHaveDrunkBeerCans(String arg0) {

  }

  @When("I go to my fridge")
  public void iGoToMyFridge() {

  }

  @Then("I should have <in stock> beer cans")
  public void iShouldHaveInStockBeerCans() {
  }
}
