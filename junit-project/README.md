# junit

[https://junit.org/junit5/docs/current/user-guide/](https://junit.org/junit5/docs/current/user-guide/)

IntelliJ IDEA supports running tests on the JUnit Platform since version 2016.2. For details please see the [post on the IntelliJ IDEA blog](https://blog.jetbrains.com/idea/2016/08/using-junit-5-in-intellij-idea/). Note, however, that it is recommended to use IDEA 2017.3 or newer since these newer versions of IDEA will download the following JARs automatically based on the API version used in the project: `junit-platform-launcher`, `junit-jupiter-engine`, and `junit-vintage-engine`.

# mockito

## 打桩

```java
when(mock.isOk()).thenReturn(true);

when(mock.isOk()).thenThrow(exception);

doReturn().when(mock).someMethod();

doNothing().when(mock).someVoidMethod();

doThrow(exception).when(mock).someVoidMethod();
```

## 断言

```java
verify(mock, times(1)).someMethod(any());

verify(mock, atLeast(1)).someMethod(any());

verify(mock, atLeastOnce()).someMethod(any());

verify(mock, never()).get(someMethod());

assertEquals(expected, actual);
```

# Q & A

## When to use @Extendwith(SpringExtension.class) or @Extendwith(MockitoExtension.class) in JUnit 5?

**When involving Spring**:

If you want to use Spring test framework features in your tests like for example ```@MockBean``` , then you have to use ```@Extendwith(SpringExtension.class)```. It replaces the deprecated JUnit 4 ```@Runwith(Spring JUnit4classRunner.class)```

**When NOT involving Spring**:

If you just want to involve Mockito and don't have to involve Spring, for example,when you just want to use the ```@Mock``` / ```@InjectMocks``` annotations, then you want to use ```@Extendwith(MockitoExtension.class) ``` as it doesn't load in a bunch of unneeded Spring stuff. It replaces the deprecated JUnit 4 ```@Runwith(MockitooUnitRunner.class) ```.

```SpringExtension``` integrates the Spring TestContext Framework into JUnit 5's Jupiter.

```MockitoExtension``` is the JUnit Jupiter equivalent of our JUnit4 ```MockitoJUnitRunner```.

