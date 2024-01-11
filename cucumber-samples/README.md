# Get Started

## Cucumber

Cucumber 是 BDD（Behavior-Driven Development，行为驱动开发）的一个自动化测试工具，使用自然语言来描述测试用例，使得 非研发（QA、PM）也可以理解甚至编写 测试用例。

官网文档：

[https://cucumber.io/docs/guides/](https://cucumber.io/docs/guides/)

[https://school.cucumber.io/courses/bdd-with-cucumber-java](https://school.cucumber.io/courses/bdd-with-cucumber-java)

Github：

[https://github.com/cucumber](https://github.com/cucumber)

[https://github.com/cucumber/cucumber-jvm/tree/main/cucumber-junit-platform-engine](https://github.com/cucumber/cucumber-jvm/tree/main/cucumber-junit-platform-engine)

## Gherkin

Gherkin 是 Cucumber 用来描述 测试用例 的语言，以下为关键字的用意与关联关系。

![](./images/20240110223509.png)

## 关键字

Cucumber 支持在 Java 注解 中使用 {关键字} 作为占位符。

在 Step 中直接写上参数，将在 Java 代码中，会把占位符对应的参数作为方法参数传递进去。

注解中声明占位符的顺序为注入方法参数的顺序。

字串类型的关键字，需要加上单引号或 双引号 作为声明。

| 关键字     | 正则                  |
| ---------- | --------------------- |
| biginteger | "-?\d+" 或者 "\d+"    |
| string     | "([^"\]*(\.[^"\]*)*)" |
| bigdecimal | "-?\d*[.,]\d+"        |
| byte       | "-?\d+" 或者 "\d+"    |
| double     | "-?\d*[.,]\d+"        |
| short      | "-?\d+" 或者 "\d+"    |
| float      | "-?\d*[.,]\d+"        |
| word       | "\w+"                 |
| int        | "-?\d+" 或者 "\d+"    |
| long       | "-?\d+" 或者 "\d+"    |

参数化、表格化、列表化 混合使用时，DataTable 与 List 必须作为 Java 方法的最后一个参数。

## 钩子方法（Hook）

| 注解        | 执行时机                     |
| ----------- | ---------------------------- |
| @BeforeAll  | 在启动 Cucumber 时执行       |
| @Before     | 在所有 Scenario 执行之前执行 |
| @BeforeStep | 在所有 Step 执行之前执行     |
| @AfterAll   | 在结束 Cucumber 时执行       |
| @After      | 在所有 Scenario 执行之后执行 |
| @AfterStep  | 在所有 Step 执行之后执行     |

# IDEA cucumber support

IDEA插件：

* Cucumber for Java

* Cucumber +

[https://www.jetbrains.com/help/idea/2023.3/cucumber-support.html](https://www.jetbrains.com/help/idea/2023.3/cucumber-support.html)

# Related links

[https://chromedriver.chromium.org/downloads](https://chromedriver.chromium.org/downloads)

[Cucumber 黄瓜测试 BDD 从入门到精通](https://juejin.cn/post/7101222168180031525)
