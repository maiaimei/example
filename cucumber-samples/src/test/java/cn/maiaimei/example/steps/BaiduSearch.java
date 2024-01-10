package cn.maiaimei.example.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class BaiduSearch {

  private static ChromeDriver chromeDriver = null;

  {
    System.setProperty("webdriver.chrome.driver",
        "E:\\software\\chromedriver-win64\\chromedriver.exe");
    chromeDriver = new ChromeDriver();
  }

  @Given("open baidu")
  public void openBaidu() throws InterruptedException {
    chromeDriver.get("https://www.baidu.com/");
    Thread.sleep(2000);
  }

  @When("input keyword {}")
  public void inputKeyword(String keyword) {
    // 获取搜索框元素
    final WebElement webElement = chromeDriver.findElement(By.name("wd"));
    // 输入关键字
    webElement.sendKeys(keyword);
    // 点击“百度一下”
    webElement.submit();
  }

  @Then("get result")
  public void getResult() throws InterruptedException {
    chromeDriver.quit();
    Thread.sleep(2000);
  }
}
