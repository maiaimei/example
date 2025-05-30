有几种方法可以在 Java 启动时设置时区：

使用 JVM 参数 `-Duser.timezone`：

```bash
# 设置为 UTC
java -Duser.timezone=UTC MainClass

# 设置为北京时间
java -Duser.timezone=Asia/Shanghai MainClass

# 设置为美国东部时间
java -Duser.timezone=America/New_York MainClass
```

在代码中通过 `System.setProperty()` 设置：

```java
// 设置为 UTC
System.setProperty("user.timezone","UTC");

// 设置为北京时间
System.setProperty("user.timezone","Asia/Shanghai");
```

使用 TimeZone 类设置：

```java
// 设置为 UTC
TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

// 设置为北京时间
TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
```

验证当前时区设置：

```java
// 验证时区设置
TimeZone tz = TimeZone.getDefault();
System.out.printf("当前时区: DisplayName = %s, ID = %s, offset = %d%n",tz.getDisplayName(), tz.getID(),tz.getRawOffset());
```

一些重要说明：

1. 时区 ID 的格式通常是 "区域/城市" 的形式，如 "Asia/Shanghai"

2. 常用的时区 ID：

   - UTC


   - Asia/Shanghai (北京时间)


   - America/New_York


   - Europe/London


   - Asia/Tokyo


3. 建议：

   - 在应用启动最开始就设置时区


   - 优先使用 JVM 参数的方式设置


   - 在容器环境中，可以通过环境变量设置


   - 生产环境建议统一使用 UTC 时区，在展示层再转换为本地时区


4. 在 Docker 环境中设置时区：

   ```dockerfile
   # Dockerfile 中设置时区
   ENV TZ=Asia/Shanghai
   ```

   或者在 docker run 时设置：

   ```bash
   docker run -e "TZ=Asia/Shanghai" your-image
   ```

5. Spring Boot 应用中设置时区：

   ```properties
   # application.properties
   
   spring.jackson.time-zone=Asia/Shanghai
   ```

记住，修改时区设置会影响整个 JVM 的时间处理，应该谨慎使用，并确保在应用初始化的早期就完成设置。