package cn.maiaimei.example.features.web;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ChromeSample {

  private static ChromeDriver chromeDriver = null;

  {
    System.setProperty("webdriver.chrome.driver",
        "E:\\software\\chromedriver-win64\\chromedriver.exe");
    // 打开浏览器
    chromeDriver = new ChromeDriver();
    // 浏览器最大化
    chromeDriver.manage().window().maximize();
    // 超时等待30秒
    final Duration duration = Duration.ofSeconds(30);
    chromeDriver.manage().timeouts().implicitlyWait(duration);
  }

  @Given("open baidu")
  public void openBaidu() throws InterruptedException {
    // 跳转到百度
    chromeDriver.get("https://www.baidu.com/");
  }

  @When("input keyword {}")
  public void inputKeyword(String keyword) {
    // 获取搜索框
    final WebElement wdElement = chromeDriver.findElement(By.name("wd"));
    // 点击输入框
    wdElement.click();
    // 清空输入框
    wdElement.clear();
    // 输入关键字
    wdElement.sendKeys(keyword);
    // 点击“百度一下”
    final WebElement suElement = chromeDriver.findElement(By.id("su"));
    suElement.click();
  }

  @Then("get result")
  public void getResult() throws InterruptedException {
    Thread.sleep(2000);
    chromeDriver.quit();
  }
}
