[黄瓜（Cucumber）](https://cucumber.io/)是一个基于行为驱动开发（BDD）的测试框架，它允许程序员使用自然语言编写测试用例，这样更容易理解测试意图和设计。

[https://github.com/cucumber/cucumber-jvm/tree/main/cucumber-junit-platform-engine](https://github.com/cucumber/cucumber-jvm/tree/main/cucumber-junit-platform-engine)

# 环境准备

IDEA安装插件：

* Cucumber for Java
* Cucumber +

[https://www.jetbrains.com/help/idea/2023.3/cucumber-support.html](https://www.jetbrains.com/help/idea/2023.3/cucumber-support.html)

在 pom.xml 文件中添加 Cucumber 依赖：

```xml

<dependencies>
  <!-- https://mvnrepository.com/artifact/io.cucumber/cucumber-core -->
  <dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>cucumber-core</artifactId>
    <version>7.15.0</version>
  </dependency>
  <!-- https://mvnrepository.com/artifact/io.cucumber/cucumber-java -->
  <dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>cucumber-java</artifactId>
    <version>7.15.0</version>
  </dependency>
  <!-- https://mvnrepository.com/artifact/io.cucumber/cucumber-junit-platform-engine -->
  <dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>cucumber-junit-platform-engine</artifactId>
    <version>7.15.0</version>
    <scope>test</scope>
  </dependency>
</dependencies>
```

# Cucumber + Junit5

[https://blog.csdn.net/qq_16422103/article/details/132133552](https://blog.csdn.net/qq_16422103/article/details/132133552)

[junit5数据驱动](https://www.cnblogs.com/Durant0420/p/14788027.html)

[chromedriver驱动的下载、安装](https://zhuanlan.zhihu.com/p/664339667)

[https://chromedriver.chromium.org/downloads](https://chromedriver.chromium.org/downloads)

[https://www.bilibili.com/video/BV1Xr4y1p7J1?
p=4&spm_id_from=pageDriver&vd_source=80612925dae54b29d86f65198f1081f4](https://www.bilibili.com/video/BV1Xr4y1p7J1?p=4&spm_id_from=pageDriver&vd_source=80612925dae54b29d86f65198f1081f4)
