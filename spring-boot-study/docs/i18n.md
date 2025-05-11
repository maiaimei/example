# 国际化支持

## Spring Boot实现国际化方案

1. 首先在 `pom.xml` 确保有 Web 依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

2. 在 resources 目录下创建国际化资源文件：

```plaintext
resources/
  ├── i18n/
  │   ├── messages.properties      # 默认语言
  │   ├── messages_zh_CN.properties    # 中文
  │   └── messages_en_US.properties    # 英文
```

3. 编写资源文件内容：

```properties
# messages.properties (默认)
welcome=欢迎
user.name=用户名
user.password=密码

# messages_zh_CN.properties
welcome=欢迎
user.name=用户名
user.password=密码

# messages_en_US.properties
welcome=Welcome
user.name=Username
user.password=Password
```

4. 配置国际化支持（创建配置类）：

```java
@Configuration
public class InternationalizationConfig {
    
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        // 设置默认区域
        resolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return resolver;
    }
    
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        // 指定国际化资源文件路径
        messageSource.setBasename("classpath:i18n/messages");
        // 指定编码
        messageSource.setDefaultEncoding("UTF-8");
        // 设置缓存时间（单位：秒）
        messageSource.setCacheSeconds(3600);
        return messageSource;
    }
    
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        // 指定切换语言的参数名
        interceptor.setParamName("lang");
        return interceptor;
    }
    
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(localeChangeInterceptor());
            }
        };
    }
}
```

5. 在 application.properties 中添加配置：

```properties
# 默认语言
spring.messages.basename=i18n/messages
spring.messages.encoding=UTF-8
spring.messages.cache-duration=3600
```

6. 创建工具类方便使用：

```java
@Component
public class I18nUtil {
    
    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private LocaleResolver localeResolver;
    
    @Autowired
    private HttpServletRequest request;
    
    /**
     * 获取国际化消息
     */
    public String getMessage(String code) {
        return getMessage(code, null);
    }
    
    /**
     * 获取国际化消息带参数
     */
    public String getMessage(String code, Object[] args) {
        Locale locale = localeResolver.resolveLocale(request);
        return messageSource.getMessage(code, args, locale);
    }
}
```

7. Controller 使用示例：

```java
@RestController
@RequestMapping("/api")
public class DemoController {
    
    @Autowired
    private I18nUtil i18nUtil;
    
    @GetMapping("/welcome")
    public String welcome() {
        return i18nUtil.getMessage("welcome");
    }
    
    @GetMapping("/hello")
    public String hello(@RequestParam String name) {
        // 使用带参数的消息
        return i18nUtil.getMessage("hello.name", new Object[]{name});
    }
    
    // 返回统一响应对象的示例
    @GetMapping("/user")
    public ApiResponse<UserDTO> getUser() {
        return new ApiResponse<>(
            ResponseCode.SUCCESS.getCode(),
            i18nUtil.getMessage("user.get.success"),
            userDTO
        );
    }
}
```

8. 前端切换语言示例：

```javascript
// 切换语言只需要在请求URL后面加上 lang 参数
// 例如：/api/welcome?lang=en_US
// 或者：/api/welcome?lang=zh_CN

async function switchLanguage(lang) {
    try {
        // 保存用户语言偏好
        localStorage.setItem('preferredLanguage', lang);
        // 刷新当前页面内容
        await loadPageContent(lang);
    } catch (error) {
        console.error('Failed to switch language:', error);
    }
}
```

9. 支持 ResponseBody 注解的国际化：

```java
@ControllerAdvice
public class ResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    
    @Autowired
    private I18nUtil i18nUtil;
    
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
    
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {
        if (body instanceof ApiResponse) {
            ApiResponse<?> apiResponse = (ApiResponse<?>) body;
            // 转换消息key为对应语言的消息
            String message = i18nUtil.getMessage(apiResponse.getMessageKey());
            apiResponse.setMessage(message);
        }
        return body;
    }
}
```

10. 异常处理中使用国际化：

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @Autowired
    private I18nUtil i18nUtil;
    
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception e) {
        return new ApiResponse<>(
            ResponseCode.INTERNAL_ERROR.getCode(),
            i18nUtil.getMessage("system.error"),
            null
        );
    }
    
    @ExceptionHandler(ValidationException.class)
    public ApiResponse<Void> handleValidationException(ValidationException e) {
        return new ApiResponse<>(
            ResponseCode.PARAM_ERROR.getCode(),
            i18nUtil.getMessage("validation.error", new Object[]{e.getMessage()}),
            null
        );
    }
}
```

主要特点和建议：

1. 支持多种语言切换方式：
   - URL参数切换
   - Header切换
   - Session存储
   - Cookie存储
2. 缓存配置：
   - 适当设置缓存时间，避免频繁读取文件
   - 开发环境可以设置较短缓存时间方便调试
3. 资源文件管理：
   - 按模块分类管理
   - 使用统一的命名规范
   - 保持各语言文件的key一致
4. 性能优化：
   - 使用缓存
   - 避免频繁创建Locale对象
   - 考虑使用消息格式化缓存
5. 安全考虑：
   - 防止注入攻击
   - 验证语言参数合法性
   - 限制消息参数数量

这样的实现既完整又灵活，能够满足大多数项目的国际化需求。

## 前后端实现国际化方案

1. 首先定义统一的响应结构:

```java
// 后端响应结构
public class ApiResponse<T> {
    private int code;           // 响应码
    private String messageKey;  // 消息key，用于前端国际化
    private T data;            // 响应数据
    
    // 构造方法、getter和setter省略
}
```

2. 后端定义响应码枚举:

```java
public enum ResponseCode {
    SUCCESS(2000, "response.success"),
    PARAM_ERROR(4000, "response.param.error"),
    UNAUTHORIZED(4001, "response.unauthorized"),
    FORBIDDEN(4003, "response.forbidden"),
    NOT_FOUND(4004, "response.not.found"),
    INTERNAL_ERROR(5000, "response.internal.error");

    private final int code;
    private final String messageKey;

    ResponseCode(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }
    
    // getter方法省略
}
```

3. 前端定义国际化消息文件:

```javascript
// i18n/en.js - 英文
export default {
  response: {
    success: 'Operation successful',
    'param.error': 'Invalid parameter',
    unauthorized: 'Please login first',
    forbidden: 'No permission',
    'not.found': 'Resource not found',
    'internal.error': 'System error'
  }
}

// i18n/zh.js - 中文
export default {
  response: {
    success: '操作成功',
    'param.error': '参数错误',
    unauthorized: '请先登录',
    forbidden: '没有权限',
    'not.found': '资源不存在',
    'internal.error': '系统错误'
  }
}
```

4. 后端使用示例:

```java
@RestController
public class UserController {
    
    @GetMapping("/user/{id}")
    public ApiResponse<UserDTO> getUser(@PathVariable Long id) {
        try {
            UserDTO user = userService.getUser(id);
            if (user == null) {
                return new ApiResponse<>(ResponseCode.NOT_FOUND);
            }
            return new ApiResponse<>(ResponseCode.SUCCESS, user);
        } catch (Exception e) {
            return new ApiResponse<>(ResponseCode.INTERNAL_ERROR);
        }
    }
}
```

5. 前端处理示例 (使用Vue + i18n):

```javascript
// 封装请求响应拦截器
import i18n from '@/i18n'
import axios from 'axios'

// 创建axios实例
const request = axios.create({
  baseURL: '/api',
  timeout: 5000
})

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    
    // 处理成功响应
    if (res.code === 2000) {
      return res.data
    }
    
    // 显示错误消息
    const message = i18n.t(res.messageKey)
    showErrorMessage(message)
    
    // 特殊错误码处理
    if (res.code === 4001) {
      // 未登录，跳转登录页
      router.push('/login')
    }
    
    return Promise.reject(new Error(message))
  },
  error => {
    showErrorMessage(i18n.t('response.internal.error'))
    return Promise.reject(error)
  }
)

// 使用示例
async function getUser(id) {
  try {
    const user = await request.get(`/user/${id}`)
    // 处理成功响应
  } catch (error) {
    // 错误已经被拦截器处理
    console.error(error)
  }
}
```

这个方案的主要优点：

1. 统一的响应结构，便于前端处理
2. 使用消息key而不是直接返回消息文本，支持前端国际化
3. 响应码集中管理，便于维护
4. 前端统一的错误处理机制
5. 支持多语言切换

建议：

1. 响应码设计要有规律，比如按模块划分
2. 消息key要有命名规范，便于管理
3. 可以根据需要扩展响应结构，如添加详细错误信息字段
4. 考虑添加请求追踪ID，便于问题排查
5. 可以实现响应码到HTTP状态码的映射
6. 建议将错误处理和国际化配置做成可配置的中间件

这样的设计既保证了系统的可维护性，又提供了良好的用户体验。

