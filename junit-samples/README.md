# JUnit

[https://junit.org/junit5/docs/current/user-guide/](https://junit.org/junit5/docs/current/user-guide/)

IntelliJ IDEA supports running tests on the JUnit Platform since version 2016.2. For details please see the [post on the IntelliJ IDEA blog](https://blog.jetbrains.com/idea/2016/08/using-junit-5-in-intellij-idea/). Note, however, that it is recommended to use IDEA 2017.3 or newer since these newer versions of IDEA will download the following JARs automatically based on the API version used in the project: `junit-platform-launcher`, `junit-jupiter-engine`, and `junit-vintage-engine`.

JUnit3中测试用例需要继承TestCase类

JUnit4中测试用例无需继承TestCase类，只需标记@Test注解即可。

|                          | JUnit3     | JUnit4       | JUnit5      |
| ------------------------ | ---------- | ------------ | ----------- |
| 所有测试用例前仅执行一次 | N/A        | @BeforeClass | @BeforeAll  |
| 每个测试用例前执行       | setUp()    | @Before      | @BeforeEach |
| 执行测试用例             | N/A        | @Test        | @Test       |
| 每个测试用例后执行       | tearDown() | @After       | @AfterEach  |
| 所有测试用例后仅执行一次 | N/A        | @AfterClass  | @AfterAll   |

## Junit 5 

### 参数化测试（数据驱动测试）

```xml
<!-- Junit 5的执行引擎 -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <version>5.8.2</version>
    <scope>runtime</scope>
</dependency>
<!-- 用于提供Junit 5参数化测试的能力 -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-params</artifactId>
    <version>5.8.2</version>
    <scope>compile</scope>
</dependency> 
```

参数化测试必须将 @Test 注解替换为 @ParameterizedTest 注解。

- @ValueSource 注解可提供单个数组作为测试数据。
- @NullSource 注解用于传递null值。
- @EmptySource 注解用于传递空值。
- @NullAndEmptySource 注解是 @NullSource 和 @EmptySource 的组合注解，可同时提供null值和空值。
- @EnumSource 注解可提供单个枚举作为测试数据。
- @MethodSource 注解读取指定方法的返回值作为参数化测试的入参（注意方法返回类型必须是一个流）。
- @CsvSource 注解可提供单组或多组参数。
- @CsvFileSource 注解通过读取CSV文件作为参数化测试的入参。
- 实现 ArgumentsProvider 接口，并配合 @ArgumentsSource 注解来给测试方法提供测试数据。

# Mockito

[https://site.mockito.org/](https://site.mockito.org/)

- [`mock()`](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#mock-java.lang.Class-)/[`@Mock`](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mock.html): create mock
  - optionally specify how it should behave via [`Answer`](http://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/stubbing/Answer.html)/[`MockSettings`](http://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/MockSettings.html)
  - [`when()`](http://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#when-T-)/[`given()`](http://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/BDDMockito.html#given-T-) to specify how a mock should behave
  - If the provided answers don’t fit your needs, write one yourself extending the [`Answer`](http://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/stubbing/Answer.html) interface
- [`spy()`](http://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#spy-T-)/[`@Spy`](http://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Spy.html): partial mocking, real methods are invoked but still can be verified and stubbed
- [`@InjectMocks`](http://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/InjectMocks.html): automatically inject mocks/spies fields annotated with `@Spy` or `@Mock`
- [`verify()`](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#verify-T-): to check methods were called with given arguments
  - can use flexible argument matching, for example any expression via the [`any()`](http://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/ArgumentMatchers.html#any--)
  - or capture what arguments were called using [`@Captor`](http://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Captor.html) instead

What are the limitations of Mockito: [https://github.com/mockito/mockito/wiki/FAQ](https://github.com/mockito/mockito/wiki/FAQ)

## stubbing

```java
// 有返回值
when(mockObject.someMethod()).thenReturn(someReturnValue);
doReturn(someReturnValue).when(mockObject).someMethod();

// 无返回值
doNothing().when(mockObject).someMethod();

// 抛出异常
when(mockObject.someMethod()).thenThrow(someException);
doThrow(someException).when(mockObject).someMethod();
```

## assertion

```java
// org.junit.jupiter.api.Assertions
// 方法是否被调用
verify(mockObject, never()).someMethod("never called");
verify(mockObject, atLeastOnce()).someMethod("called at least once");
verify(mockObject, atLeast(2)).someMethod("called at least twice");
verify(mockObject, atMost(3)).someMethod("called at most three times");
verify(mockObject, times(4)).someMethod("called four times");
```

# PowerMock

[http://powermock.github.io/](http://powermock.github.io/)

[https://www.cnblogs.com/zhu-tingting/p/7359756.html](https://www.cnblogs.com/zhu-tingting/p/7359756.html)

[https://blog.csdn.net/qq_39273039/article/details/107653810](https://blog.csdn.net/qq_39273039/article/details/107653810)

# Q & A

## When to use @Extendwith(SpringExtension.class) or @Extendwith(MockitoExtension.class) in JUnit 5?

**When involving Spring**:

If you want to use Spring test framework features in your tests like for example ```@MockBean``` , then you have to use ```@Extendwith(SpringExtension.class)```. It replaces the deprecated JUnit 4 ```@Runwith(Spring JUnit4classRunner.class)```

**When NOT involving Spring**:

If you just want to involve Mockito and don't have to involve Spring, for example,when you just want to use the ```@Mock``` / ```@InjectMocks``` annotations, then you want to use ```@Extendwith(MockitoExtension.class) ``` as it doesn't load in a bunch of unneeded Spring stuff. It replaces the deprecated JUnit 4 ```@Runwith(MockitooUnitRunner.class) ```.

```SpringExtension``` integrates the Spring TestContext Framework into JUnit 5's Jupiter.

```MockitoExtension``` is the JUnit Jupiter equivalent of our JUnit4 ```MockitoJUnitRunner```.

# Allure

[Allure](https://allurereport.org/)是一个开源的测试报告框架。

Allure生成测试报告分两步：

1. 收集测试结果数据。Allure适配器保存测试执行信息到XML文件中。其适配器支持与Java、Python等语言中流行的测试框架集成。对于Java而言，Allure可与JUnit4、JUnit5、Cucumber JVM集成。
2. 生成测试报告。通过命令行工具、CI插件或构建工具将XML文件转换为HTML测试报告。

## 安装Allure

[https://allurereport.org/docs/gettingstarted-installation/](https://allurereport.org/docs/gettingstarted-installation/)

## 收集测试结果数据

Allure JUnit 5：[https://allurereport.org/docs/junit5/](https://allurereport.org/docs/junit5/)

执行测试用例

```shell
# 执行所有符合条件的测试用例
mvn test

# 执行某个class文件
mvn test -Dtest=xxxx
mvn clean test -Dtest=UserServiceTest
```

## 生成测试报告

### 使用命令行工具生成测试报告

allure 常用命令

```shell
allure --help 帮助
allure --version 查看版本信息  
allure serve 生成在线版本的测试
allure generate <allure-result中间文件> -o 输出目录 (默认路径：allure-report)
```

```shell
cd target
allure serve allure-results
```

使用以上命令生成并打开测试报告时，Allure会默认使用Jetty作为服务器，并使用默认浏览器打开测试报告。
