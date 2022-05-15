[http://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/](http://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)

[https://mybatis.org/mybatis-3/](https://mybatis.org/mybatis-3/)

[https://blog.mybatis.org/](https://blog.mybatis.org/)

[MyBatis-Plus](https://baomidou.com/)

<span style="color:red; font-weight:bold;">主键生成策略和自定义主键生成器</span>

```java
@MapperScan("cn.maiaimei.uuap.mapper")
@Configuration
public class MybatisPlusConfig {
    /**
     * 自定义ID生成器
     */
    @Bean
    public IdentifierGenerator identifierGenerator() {
        return new CustomIdGenerator();
    }

    public class CustomIdGenerator implements IdentifierGenerator {
        @Override
        public Number nextId(Object entity) {
            return SFID.nextId();
        }
    }
}
```

```java
@TableName("sys_user")
public class UserEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @TableField
    private String username;
}
```

```java
public enum IdType {
    AUTO(0),
    NONE(1),
    INPUT(2),
    ASSIGN_ID(3),
    ASSIGN_UUID(4);
}
```

| 值          | 描述                                                         |
| ----------- | ------------------------------------------------------------ |
| AUTO        | 数据库自增ID                                                 |
| NONE        | 未设置主键类型，相当于INPUT                                  |
| INPUT       | 手动插入ID                                                   |
| ASSIGN_ID   | 分配ID（since 3.3.0）<br/>主键类型为Number（Long和Integer）<br/>使用接口IdentifierGenerator的方法nextId，默认实现类为DefaultIdentifierGenerator雪花算法 |
| ASSIGN_UUID | 分配UUID（since 3.3.0）<br/>主键类型为String<br/>使用接口IdentifierGenerator的方法nextUUID |