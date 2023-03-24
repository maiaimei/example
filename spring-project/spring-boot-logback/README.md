[https://logback.qos.ch/](https://logback.qos.ch/)



SpringBoot 默认日志等级为 info,也就是说只在控制台输出 info 或更高等级的日志。

```yaml
logging:
  # 设置日志级别，root表示整个项目，可以指定到某个包下，也可以具体到某个类名（日志级别的值不区分大小写）
  level:
    # 指定某个包下的日志级别，多个包不同日志级别写多行即可
    cn.maiaimei.example.controller: trace
    cn.maiaimei.example.service: debug
```



logback.xml, logback-spring.xml, logback-test.xml区别

logback.xml, logback-spring.xml, logback-test.xml都可以配置logback。

logback-spring.xml 只有在Spring应用程序运行的时候才生效，即带有@SpringBootApplication注解的类启动的时候才会生效。如果不是Spring应用程序，而是一个main方法或者一个JUnit的测试方法，要用 logback.xml 来配置。

logback.xml加载早于application.properties，如果在logback.xml中使用了application.properties配置，则获取不到，改成logback-spring.xml可以解决。加载顺序如下：
logback.xml -> application.properties -> logback-spring.xml

Let us begin by discussing the **initialization steps** that **logback** follows to try to configure itself:

1. Logback will search for any custom Configurator providers using service-provider loading facility (introduced in JDK 1.6). If any such custom provider is found, it takes precedence over logback's own DefaultJoranConfigurator (see next).

   A custom Configurator is an implementation of ch.qos.logback.classic.spi.Configurator interface. Custom configurators are searched by looking up file resources located under META-INF/services/ch.qos.logback.classic.spi.Configurator. The contents of this file should specify the fully qualified class name of the desired Configurator implementation.

2. If no custom Configurator was found in the previous step, then DefaultJoranConfigurator will be instantiated and invoked.
   * If the system property "logback.configurationFile" is set, then DefaultJoranConfigurator will try to locate the file specified the aforementioned system property.
   *  **If the previous step fails, DefaultJoranConfigurator will try to locate the configuration file logback-test.xml in the classpath.**
   *  **If no such file is found, it will try to locate the configuration file logback.xml in the classpath. Note that this is the nominal configuration step.**
   *  If neither file can be located, DefaultJoranConfigurator will return without further processing.

3. If none of the above succeeds, logback-classic will configure itself automatically using the BasicConfigurator which will cause logging output to be directed to the console.
