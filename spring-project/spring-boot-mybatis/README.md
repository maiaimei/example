# spring-boot-mybatis

[https://mybatis.org/mybatis-3/](https://mybatis.org/mybatis-3/)

[https://mybatis.org/mybatis-3/zh/index.html](https://mybatis.org/mybatis-3/zh/index.html)

[https://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/](https://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)

## H2数据库连接

内存模式

```
jdbc:h2:mem:<databaseName>
```

以嵌入式(本地)连接方式连接H2数据库

```
jdbc:h2:[file:][<path>]<databaseName>

jdbc:h2:~/test    		// 连接位于用户目录下的test数据库
jdbc:h2:file:/data/test
jdbc:h2:file:E:/data/test  // Windows only
```

使用TCP/IP的服务器模式(远程连接)方式连接H2数据库(推荐)

```
jdbc:h2:tcp://<server>[:<port>]/[<path>]<databaseName>
jdbc:h2:tcp://localhost/~/test
```

