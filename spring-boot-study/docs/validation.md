默认的验证消息文件位于 hibernate-validator-*.jar 的 org/hibernate/validator/ValidationMessages.properties 路径下。

您可以通过创建自己的 ValidationMessages.properties 文件来覆盖这些默认消息，而无需修改 JAR 包中的内容。

有几种方式可以指定 Spring Boot Validation 的消息文件路径：

1. 通过配置类指定：

```java
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidationConfig {

    /**
     * 配置消息源
     */
    @Bean
    public MessageSource validationMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        // 指定验证消息文件路径
        messageSource.setBasename("validation/messages");
        // 指定多个验证消息文件路径
        messageSource.setBasenames(
            "validation/common",
            "validation/user",
            "validation/order"
        );
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * 配置验证器工厂
     */
    @Bean
    public LocalValidatorFactoryBean validator(MessageSource validationMessageSource) {
        LocalValidatorFactoryBean validatorFactory = new LocalValidatorFactoryBean();
        validatorFactory.setValidationMessageSource(validationMessageSource);
        return validatorFactory;
    }
}
```

2. 通过 application.properties/yml 配置：

```yaml
spring:
  messages:
    # 指定单个验证消息文件
    basename: validation/messages
    # 或指定多个验证消息文件
    basename: validation/common,validation/user,validation/order
    encoding: UTF-8
```

3. 文件组织示例：

```plaintext
src/
└── main/
    └── resources/
        ├── validation/
        │   ├── common.properties      # 通用验证消息
        │   ├── user.properties        # 用户相关验证消息
        │   └── order.properties       # 订单相关验证消息
        └── application.yml
```

4. 各验证消息文件内容示例：

```properties
# common.properties
validation.notblank=不能为空
validation.length=长度必须在{min}到{max}之间
validation.email=邮箱格式不正确
validation.phone=手机号格式不正确

# user.properties
user.username.notblank=用户名不能为空
user.username.length=用户名长度必须在{min}到{max}之间
user.password.pattern=密码必须包含字母和数字，且长度不少于8位
user.email.format=邮箱格式不正确
user.phone.format=手机号格式不正确

# order.properties
order.id.notblank=订单号不能为空
order.amount.range=订单金额必须在{min}到{max}之间
order.status.invalid=订单状态不正确
```

5. 在实体类中使用：

```java
@Data
public class UserDTO {
    
    @NotBlank(message = "{user.username.notblank}")
    @Length(min = 4, max = 20, message = "{user.username.length}")
    private String username;

    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
        message = "{user.password.pattern}"
    )
    private String password;

    @NotBlank(message = "{user.email.format}")
    @Email(message = "{user.email.format}")
    private String email;

    @Pattern(
        regexp = "^1[3-9]\\d{9}$",
        message = "{user.phone.format}"
    )
    private String phone;
}

@Data
public class OrderDTO {
    
    @NotBlank(message = "{order.id.notblank}")
    private String orderId;

    @Range(min = 0, max = 999999, message = "{order.amount.range}")
    private BigDecimal amount;

    @NotNull(message = "{validation.notblank}")
    private OrderStatus status;
}
```

6. 自定义验证注解时使用：

```java
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
public @interface Phone {
    
    String message() default "{validation.phone}";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
```

7. 在验证器中使用：

```java
@Component
public class PhoneValidator implements ConstraintValidator<Phone, String> {
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            return true; // 为空的验证交给@NotBlank处理
        }
        return value.matches("^1[3-9]\\d{9}$");
    }
}
```

8. 如果需要动态加载或热更新：

```java
@Configuration
@RefreshScope // 支持配置热更新
public class DynamicValidationConfig {

    @Value("${validation.message.path:validation/messages}")
    private String messagePath;

    @Bean
    public MessageSource validationMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = 
            new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:" + messagePath);
        messageSource.setDefaultEncoding("UTF-8");
        // 设置缓存时间（秒）
        messageSource.setCacheSeconds(60);
        return messageSource;
    }
}
```

这种配置方式的优点：

1. 结构清晰：
   - 消息文件分类管理
   - 便于维护和扩展
   - 模块化组织
2. 配置灵活：
   - 支持多种配置方式
   - 可以动态加载
   - 支持热更新
3. 使用方便：
   - 统一的消息管理
   - 支持参数替换
   - 易于国际化
4. 可维护性好：
   - 集中管理验证消息
   - 便于修改和更新
   - 支持模块化管理

这样的配置可以很好地管理和组织验证消息，提高代码的可维护性和可扩展性。