# 设计模式

## 创建型模式

### 单例模式

单例模式（Singleton），保证一个类仅有一个实例，并提供一个访问它的全局点。

饿汉式单例类：静态初始化，在类加载时创建类的实例。

懒汉式单例类：在第一次调用全局访问点时创建类的实例，注意线程安全问题（双重锁定）。

### 建造者模式

建造者模式（Builder），将一个复杂对象的构建与它的表示分类，使得同样的构建过程可以创建不同的表示。

对客户端来说，使用建造者模式，可以隐藏复杂对象的构建的过程与细节。

应用：JDK的StringBuilder

### 简单工厂模式

### 抽象工厂模式

### 工厂方法模式

## 结构型模式

### 代理模式

代理模式（Proxy），为其他对象提供一种代理以控制对这个对象的访问。

特点：控制目标对象的访问；增强目标对象的功能。

分类：静态代理和动态代理（JDK代理、CGLib代理）。

应用：Spring的AOP、Mybatis

[为什么说没有代理模式就没有Spring和MyBatis，一节课轻松解答](https://www.bilibili.com/video/BV1aq4y1j7ti)

### 外观模式



## 行为型模式

# PlantUML

[https://plantuml.com/class-diagram](https://plantuml.com/class-diagram)

[https://plantuml.com/zh/class-diagram](https://plantuml.com/zh/class-diagram)

