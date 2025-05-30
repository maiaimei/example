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