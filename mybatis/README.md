# Basic concepts

MyBatis 和 Spring 集成中的核心组件之间的关系图：

```
DataSource
    ↓
SqlSessionFactory
    ↓
SqlSessionTemplate
    ↓
Mapper/DAO

DataSource → DataSourceTransactionManager
```

## DataSource

```java
@Bean
public DataSource dataSource() {
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setUrl("jdbc:mysql://localhost:3306/test");
    dataSource.setUsername("root");
    dataSource.setPassword("root");
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    return dataSource;
}
```

- 最基础的组件，提供数据库连接，负责数据库连接的获取和释放
- 管理数据库连接池，可以使用不同的实现（如 Druid、HikariCP、DBCP 等）

## SqlSessionFactory

```java
@Bean
public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    // 设置数据源
    factoryBean.setDataSource(dataSource);
    
    // 设置 MyBatis 配置
    Configuration configuration = new Configuration();
    configuration.setMapUnderscoreToCamelCase(true);
    configuration.setCacheEnabled(true);
    factoryBean.setConfiguration(configuration);
    
    // 设置映射文件位置
    factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
            .getResources("classpath:mapper/*.xml"));
            
    return factoryBean.getObject();
}
```

- 用于创建 SqlSession
- 管理 MyBatis 的全局配置
- 依赖于 DataSource
- 负责加载 Mapper 文件和配置
- 线程安全，可以在多个线程间共享

## SqlSessionTemplate

```java
@Bean
public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
    return new SqlSessionTemplate(sqlSessionFactory);
}
```

- Spring 对 MyBatis SqlSession 的封装
- 线程安全的，可以被多个 DAO 或 Mapper 共享
- 依赖于 SqlSessionFactory
- 管理 SqlSession 的生命周期
- 处理异常转换
- 集成了 Spring 的事务管理

## DataSourceTransactionManager

```java
@Bean
public DataSourceTransactionManager transactionManager(DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
}
```

- Spring 的事务管理器
- 依赖于 DataSource，与特定数据源绑定
- 实际执行事务操作的管理器，管理事务的具体行为，负责事务的开启、提交、回滚
- 配合 @Transactional 注解使用

@EnableTransactionManagement：启用注解事务管理

- 作用：
  - 启用基于注解的事务管理功能
  - 开启事务注解的解析
  - 注册事务相关的切面和增强器
  - 使 @Transactional 注解生效
- 原理：
  - 导入 TransactionManagementConfigurationSelector
  - 注册事务处理器（AOP）
  - 创建事务拦截器
  - 配置事务属性源

# Basic configuration

MyBatis 在 Spring Boot 中的配置可以在以下几个地方查看：

1. 官方配置属性（application.properties/yml）：

   ```yaml
   mybatis:
     # 指定映射文件位置
     mapper-locations: classpath:mapper/*.xml
     # 指定实体类包路径
     type-aliases-package: com.example.domain
     configuration:
       # 开启驼峰命名转换
       map-underscore-to-camel-case: true
       # SQL执行超时时间（秒）
       default-statement-timeout: 30
       # 打印SQL日志
       log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
       # 开启二级缓存
       cache-enabled: true
   ```

2. 通过Java配置类：

   ```java
   @Configuration
   public class MyBatisConfig {
       @Bean
       public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
           SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
           sessionFactory.setDataSource(dataSource);
           
           // 设置映射文件位置
           sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                   .getResources("classpath:mapper/*.xml"));
           
           // 设置配置属性
           org.apache.ibatis.session.Configuration configuration = 
                   new org.apache.ibatis.session.Configuration();
           configuration.setMapUnderscoreToCamelCase(true);
           sessionFactory.setConfiguration(configuration);
           
           return sessionFactory.getObject();
       }
   }
   ```

3. 常用配置项说明：

   ```yaml
   mybatis:
     # 核心配置
     config-location: classpath:mybatis-config.xml  # MyBatis核心配置文件位置
     mapper-locations: classpath:mapper/**/*.xml    # 映射文件位置
     type-aliases-package: com.example.domain      # 实体类包路径
     
     configuration:
       # 缓存配置
       cache-enabled: true                         # 开启二级缓存
       local-cache-scope: session                  # 本地缓存作用域
       
       # 映射配置
       map-underscore-to-camel-case: true         # 驼峰命名转换
       auto-mapping-behavior: partial              # 自动映射行为
       
       # 执行配置
       default-statement-timeout: 30               # SQL超时时间
       default-fetch-size: 100                     # 数据集大小
       
       # 日志配置
       log-impl: org.apache.ibatis.logging.stdout.StdOutImpl  # 日志实现
       
       # 执行器配置
       default-executor-type: simple               # 执行器类型
       
       # 其他配置
       lazy-loading-enabled: false                 # 延迟加载
       aggressive-lazy-loading: false              # 积极加载
       multiple-result-sets-enabled: true          # 多结果集
   ```

4. 分页插件配置：

   ```java
   @Configuration
   public class MybatisPlusConfig {
       @Bean
       public PaginationInterceptor paginationInterceptor() {
           PaginationInterceptor interceptor = new PaginationInterceptor();
           // 设置最大单页限制数量
           interceptor.setLimit(500);
           return interceptor;
       }
   }
   ```

5. 数据源配置：

   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/dbname
       username: root
       password: root
       driver-class-name: com.mysql.cj.jdbc.Driver
   ```

常用配置建议：

1. 建议开启驼峰命名转换（map-underscore-to-camel-case）
2. 开发环境建议配置SQL日志打印
3. 根据需要配置合适的超时时间
4. 配置合理的分页大小限制
5. 生产环境注意关闭详细日志输出
6. 根据实际需求配置缓存策略

调试技巧：

1. 开启SQL日志：

   ```yaml
   logging:
     level:
       your.package.mapper: debug
   ```

2. 显示SQL参数：

   ```yaml
   mybatis:
     configuration:
       log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
   ```

这些配置可以根据实际项目需求进行调整和优化。建议在开发时查看官方文档以获取最新的配置选项和最佳实践。

# Dynamic SQL Construction

```
org.example.common.query
    ├── Queryable.java               // 查询能力标记接口
    │
    ├── select                       // 字段选择相关
    │   ├── FieldSelectable.java     // 字段选择能力接口
    │   └── SelectableField.java     // 可选择字段定义
    │
    ├── filter                       // 过滤相关
    │   ├── Filterable.java
    │   └── FilterableItem.java
    │
    ├── page                         // 分页相关
    │   ├── Pageable.java
    │   └── PageResult.java
    │
    └── sort                         // 排序相关
        ├── Sortable.java
        └── SortableItem.java
```

# Reference

[https://baomidou.com/](https://baomidou.com/)
