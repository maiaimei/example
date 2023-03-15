# Eclipse+AmaterasUML

官方下载：[https://www.eclipse.org/downloads/packages/release](https://www.eclipse.org/downloads/packages/release)

百度网盘下载eclipse-jee-2020-12-R-win32-x86_64.zip

链接：https://pan.baidu.com/s/14GHUqzVuq41BoNPie71ZeA 
提取码：9999 

百度网盘下载AmaterasUML_1.3.4.zip

链接：https://pan.baidu.com/s/14BcQoRlwYRT56MYuJI8D5A 
提取码：9999 

1. 解压eclipse-jee-2020-12-R-win32-x86_64.zip

2. 将AmaterasUML_1.3.4.zip解压后的3个jar包拷贝到eclipse/plugins下

# IDEA+PlantUML

[https://plantuml.com/class-diagram](https://plantuml.com/class-diagram)

[https://plantuml.com/zh/class-diagram](https://plantuml.com/zh/class-diagram)

# 设计模式

[https://www.runoob.com/design-pattern/design-pattern-tutorial.html](https://www.runoob.com/design-pattern/design-pattern-tutorial.html)

<img src="/images/relation.png" />

## 创建型模式

### 单例模式

单例模式（Singleton Pattern），保证一个类仅有一个实例，并提供一个访问它的全局点。

<img src="/images/singleton.png" />

饿汉式单例类：静态初始化，在类加载时创建类的实例。

懒汉式单例类：在第一次调用全局访问点时创建类的实例，注意线程安全问题（双重锁定）。

### 原型模式

原型模式 （Prototype Pattern），通过克隆、拷贝、复制原型实例创建新的对象。

<img src="/images/prototype.png" />

深拷贝 VS 浅拷贝：

在浅拷贝中，对基本数据类型，浅拷贝会进行值传递，也就是将该属性值复制一份给新对象；对于引用数据类型，浅拷贝会进行引用传递，两个对象指向同一个地址。典型应用：{@link Object#clone()}

在深拷贝中，无论原型对象的成员变量是值类型还是引用类型，都将复制一份给克隆对象。

### 建造者模式

建造者模式（Builder Pattern），将一个复杂对象的构建与它的表示分类，使得同样的构建过程可以创建不同的表示。

<img src="/images/builder.png" />

对客户端来说，使用建造者模式，可以隐藏复杂对象的构建的过程与细节。

应用：JDK的StringBuilder，lombok 的 @Builder

### 简单工厂模式

### 抽象工厂模式

### 工厂方法模式

## 结构型模式

### 代理模式

代理模式（Proxy Pattern），为其他对象提供一种代理以控制对这个对象的访问。

抽象角色（Subject）：通过接口或抽象类声明真实角色实现的业务方法。

代理角色（ProxySubject）：实现抽象角色，是真实角色的代理，通过真实角色的业务逻辑方法来实现抽象方法，并可以附加自己的操作。

真实角色（RealSubject）：实现抽象角色，定义真实角色所要实现的业务逻辑，供代理角色调用。

<img src="/images/proxy.png" />

特点：控制目标对象的访问；增强目标对象的功能。

分类：静态代理和动态代理（JDK代理、CGLib代理）。

应用：Spring的AOP、Mybatis

[为什么说没有代理模式就没有Spring和MyBatis，一节课轻松解答](https://www.bilibili.com/video/BV1aq4y1j7ti)

### 外观模式

外观模式（Facade Pattern），也称门面模式、过程模式，为子系统中的一组接口提供一个高层接口，隐藏子系统的复杂性，实现了子系统与客户端之间的松耦合关系。

外观角色（Facade）：为多个子系统对外提供一个共同的接口。

子系统角色（Sub System）：实现系统的部分功能，客户可以通过外观角色访问它。

客户角色（Client）：通过一个外观角色访问各个子系统的功能。

<img src="/images/facade.png" />

外观模式的本质是：封装交互，简化调用。

### 装饰器模式

装饰器模式 (Decorator Pattern)，又称包装器模式 (Wrapper Pattern)，动态地给一个对象添加一些额外的职责。

<img src="/images/decorator.png" />

应用场景：

1. Java IO 类中的应用
2. MyBatis 中 Cache的应用

## 行为型模式

### 状态模式

状态模式（State Pattern），对有状态的对象，把复杂的“判断逻辑”提取到不同的状态对象中，允许一个对象在其内部状态改变时改变它的行为。对象看起来似乎修改了它的类。

<img src="/images/state.png" />

适用场景：

1. 一个对象的行为取决于它的状态，并且它必须在运行时刻根据状态改变它的行为。
2. 一个操作中含有庞大的多分支结构，并且这些分支决定于对象的状态。

### 策略模式

策略模式（Strategy Pattern），定义了算法家族，分别封装起来，让它们之间可以相互替换。

抽象策略角色（Strategy）： 策略类，通常由一个接口或者抽象类实现。

具体策略角色（ConcreteStrategy）：包装了相关的算法和行为。

环境角色（Context）：持有一个策略类的引用，最终给客户端调用。

<img src="/images/strategy.png" />

### 责任链模式

职责链、责任链模式（Chain of Responsibility Pattern），是一种处理请求的模式，多个对象通过前一对象记住其下一对象的引用而串成链，每个对象都有机会处理客户端发出的请求，直到链上某个对象处理请求成功为止。

<img src="/images/chain.png" />

应用场景：审批工作流。
