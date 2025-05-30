### HikariCP 常用配置

```yaml
spring:
  datasource:
    hikari:
      # 基础配置
      pool-name: HikariPool-1
      driver-class-name: com.mysql.cj.jdbc.Driver
      jdbc-url: jdbc:mysql://localhost:3306/test
      username: ${DB_USERNAME}
      password: ${DB_PASSWORD}
      
      # 连接池大小相关
      minimum-idle: 5                     # 最小空闲连接数
      maximum-pool-size: 15               # 最大连接数
      idle-timeout: 300000                # 空闲连接存活最大时间，默认600000（10分钟）
      max-lifetime: 1800000               # 连接的生命时长，超时而且没被使用则被释放，默认1800000（30分钟）
      
      # 连接超时相关
      connection-timeout: 20000           # 连接超时时间，默认30000（30秒）
      validation-timeout: 5000            # 连接检查超时时间，默认5000毫秒
      
      # 连接检测配置
      connection-test-query: SELECT 1     # 连接检查SQL
      keepalive-time: 60000              # 心跳检测时间
      
      # 事务相关
      auto-commit: true                   # 自动提交事务
      transaction-isolation: TRANSACTION_READ_COMMITTED  # 事务隔离级别
      
      # 性能优化
      data-source-properties:
        cachePrepStmts: true             # 开启预编译语句缓存
        prepStmtCacheSize: 250           # 预编译语句缓存大小
        prepStmtCacheSqlLimit: 2048      # 预编译语句最大长度
        useServerPrepStmts: true         # 使用服务器端预编译
        useLocalSessionState: true       # 使用本地会话状态
        rewriteBatchedStatements: true   # 批量操作优化
        cacheResultSetMetadata: true     # 缓存结果集元数据
        cacheServerConfiguration: true    # 缓存服务器配置
        elideSetAutoCommits: true        # 省略不必要的自动提交
        maintainTimeStats: false         # 不维护时间统计
```

### Druid 常用配置

```yaml
spring:
  datasource:
    druid:
      # 基础配置
      name: DruidDataSource
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/test
      username: ${DB_USERNAME}
      password: ${DB_PASSWORD}
      
      # 连接池配置
      initial-size: 5                     # 初始连接数
      min-idle: 5                         # 最小空闲连接数
      max-active: 20                      # 最大连接数
      max-wait: 60000                     # 获取连接等待超时时间
      
      # 连接检测配置
      validation-query: SELECT 1          # 检测连接是否有效的sql
      validation-query-timeout: 2000      # 检测连接是否有效的超时时间
      test-while-idle: true              # 申请连接时检测连接是否有效
      test-on-borrow: false              # 获取连接时检测连接是否有效
      test-on-return: false              # 归还连接时检测连接是否有效
      
      # 检测配置
      time-between-eviction-runs-millis: 60000  # 检测间隔时间
      min-evictable-idle-time-millis: 300000    # 连接在池中最小生存的时间
      
      # 监控统计拦截的filters
      filters: stat,wall,slf4j            # 监控统计、防火墙、日志
      
      # 慢SQL记录
      connection-properties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
      
      # StatFilter配置
      filter:
        stat:
          enabled: true
          db-type: mysql
          log-slow-sql: true
          slow-sql-millis: 2000
          
      # WallFilter配置
      filter:
        wall:
          enabled: true
          db-type: mysql
          config:
            delete-allow: true
            drop-table-allow: false
            
      # 监控页面配置
      stat-view-servlet:
        enabled: true                     # 启用监控页面
        url-pattern: /druid/*            # 访问路径
        reset-enable: true               # 允许重置统计数据
        login-username: admin            # 监控页面用户名
        login-password: ${DRUID_PASSWORD}  # 监控页面密码
        
      # WebStatFilter配置
      web-stat-filter:
        enabled: true                     # 启用WebStatFilter
        url-pattern: /*                   # 拦截所有请求
        exclusions: "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*"  # 排除一些静态资源
        session-stat-enable: true         # 开启session统计
        session-stat-max-count: 1000      # session统计最大个数
        
      # 配置密码加密
      connection-properties: config.decrypt=true;config.decrypt.key=${DRUID_PUBLIC_KEY}
      
      # 异步初始化
      async-init: true
      
      # 关闭空闲连接检测
      keep-alive: true
      
      # 打开PSCache
      pool-prepared-statements: true
      max-pool-prepared-statement-per-connection-size: 20
      
      # 事务相关
      default-auto-commit: true          # 默认自动提交
      default-transaction-isolation: 2    # 事务隔离级别
```

### 特别说明：

1. HikariCP 的优点：

   - 性能最优：号称最快的连接池
   - 代码精简：代码量相对较小，便于维护
   - 可靠性高：经过大规模生产验证

2. Druid 的优点：

   - 监控功能强大：提供详细的监控信息
   - 扩展性好：提供多个扩展点
   - 安全性高：内置防SQL注入功能
   - 支持加密：支持数据库密码加密

3. 性能优化建议：

   ```yaml
   # HikariCP 性能优化配置
   spring:
     datasource:
       hikari:
         data-source-properties:
           cachePrepStmts: true
           useServerPrepStmts: true
           prepStmtCacheSize: 250
           prepStmtCacheSqlLimit: 2048
           useLocalSessionState: true
           rewriteBatchedStatements: true
           cacheResultSetMetadata: true
           cacheServerConfiguration: true
           elideSetAutoCommits: true
           maintainTimeStats: false
   
   # Druid 性能优化配置
   spring:
     datasource:
       druid:
         initial-size: 5
         min-idle: 5
         max-active: 20
         max-wait: 60000
         time-between-eviction-runs-millis: 60000
         min-evictable-idle-time-millis: 300000
         validation-query: SELECT 1
         test-while-idle: true
         test-on-borrow: false
         test-on-return: false
         pool-prepared-statements: true
         max-pool-prepared-statement-per-connection-size: 20
         filters: stat,wall
         connection-properties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
   ```

4. 生产环境建议：

   - 使用环境变量或配置中心管理敏感信息

   - 配置合适的连接池大小

   - 启用监控和日志

   - 配置适当的超时时间

   - 启用连接池预热

   - 配置合理的检测间隔

   - 使用加密的密码

   - 配置防火墙规则

5. 监控指标：

   ```yaml
   # 重要监控指标配置
   management:
     endpoints:
       web:
         exposure:
           include: "*"
     metrics:
       tags:
         application: ${spring.application.name}
       export:
         prometheus:
           enabled: true
   ```

这些配置可以根据实际应用场景和性能需求进行调整。建议在测试环境进行压测，找到最适合的配置参数。