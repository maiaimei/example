有些客户端只能使用`GET`和`POST`这两种方法。服务器必须接受`POST`模拟其他三个方法（`PUT`、`PATCH`、`DELETE`）。

这时，客户端发出的 HTTP 请求，要加上`X-HTTP-Method-Override`属性，告诉服务器应该使用哪一个动词，覆盖`POST`方法。

> ```http
> POST /api/Person/4 HTTP/1.1  
> X-HTTP-Method-Override: PUT
> ```

上面代码中，`X-HTTP-Method-Override`指定本次请求的方法是`PUT`，而不是`POST`。

在 Spring Boot 支持 X-HTTP-Method-Override 以下是配置步骤：

1. 首先，创建一个配置类来注册 `HiddenHttpMethodFilter`：

```java
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 定义允许的 HTTP 方法
    private static final List<String> ALLOWED_METHODS =
        List.of(HttpMethod.PUT.name(), HttpMethod.PATCH.name(), HttpMethod.DELETE.name());

    private static final String REQUEST_HEADER_X_HTTP_METHOD_OVERRIDE = "X-HTTP-Method-Override";

    @Bean
    public FilterRegistrationBean<HiddenHttpMethodFilter> hiddenHttpMethodFilter() {
        FilterRegistrationBean<HiddenHttpMethodFilter> registration = new FilterRegistrationBean<>();
        HiddenHttpMethodFilter filter = new HiddenHttpMethodFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                HttpServletResponse response,
                FilterChain filterChain)
                throws ServletException, IOException {

                HttpServletRequest requestToUse = request;

                // 检查请求头是否有 X-HTTP-Method-Override，如果没有检查请求参数是否有 _method
                String methodOverride = Optional.ofNullable(request.getHeader(REQUEST_HEADER_X_HTTP_METHOD_OVERRIDE))
                    .orElse(request.getParameter(HiddenHttpMethodFilter.DEFAULT_METHOD_PARAM));
                if (HttpMethod.POST.name().equals(request.getMethod()) && Objects.nonNull(methodOverride)) {
                    String method = methodOverride.toUpperCase(Locale.ENGLISH);
                    if (ALLOWED_METHODS.contains(method)) {
                        requestToUse = new HttpMethodRequestWrapper(request, method);
                    }
                }

                filterChain.doFilter(requestToUse, response);
            }
        };
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }

    /**
     * 配置自定义 FormContentFilter 以支持
     * 1. 处理 PUT、PATCH 和 DELETE 请求中的表单数据
     * 2. 将 application/x-www-form-urlencoded 内容转换为请求参数
     * {@link WebMvcAutoConfiguration} 已经自动配置了 {@link FormContentFilter}, 只有在你需要特别定制 FormContentFilter 的行为时才使用不同的 bean 名称配置{@link FormContentFilter}
     */
    //@Bean(name = "customFormContentFilter")
    public FilterRegistrationBean<FormContentFilter> formContentFilter() {
        FilterRegistrationBean<FormContentFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new FormContentFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(2);
        return registration;
    }

    // 内部类：HTTP 方法请求包装器
    private static class HttpMethodRequestWrapper extends HttpServletRequestWrapper {
        private final String method;

        public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
            super(request);
            this.method = method;
        }

        @Override
        public String getMethod() {
            return this.method;
        }
    }
}
```

2. 使用示例：

```java
@RestController
@RequestMapping("/api")
public class TestController {
    
    @GetMapping("/test")
    public String get() {
        return "GET method";
    }
    
    @PutMapping("/test")
    public String put() {
        return "PUT method";
    }
    
    @DeleteMapping("/test")
    public String delete() {
        return "DELETE method";
    }
    
    @PatchMapping("/test")
    public String patch() {
        return "PATCH method";
    }
}
```

现在你可以通过以下方式发送请求：

1. 使用 HTTP 头：

```http
POST /api/test
X-HTTP-Method-Override: PUT
```

2. 使用表单参数：

```http
POST /api/test?_method=PUT
```

3. 如果使用 AJAX，示例如下：

```javascript
fetch('/api/test', {
    method: 'POST',
    headers: {
        'X-HTTP-Method-Override': 'PUT',
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
})
```

FormContentFilter 提供了与旧版 HttpPutFormContentFilter 相同的功能，它可以：

1. 处理 PUT、PATCH 和 DELETE 请求中的表单数据
2. 将 application/x-www-form-urlencoded 内容转换为请求参数

通过以下方式发送请求：

```javascript
// 使用 fetch 发送 PUT 请求
fetch('/api/update', {
    method: 'PUT',
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
    },
    body: 'name=John'
})

// 使用 axios 发送 PUT 请求
axios.put('/api/update', 'name=John', {
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
    }
})
```

如果你使用 Spring Security，确保配置允许这些请求：

1. 确保在安全配置中允许 X-HTTP-Method-Override 头：

```java
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .headers()
            .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
            .addHeaderWriter((request, response) -> {
                response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,PATCH");
                response.setHeader("Access-Control-Allow-Headers", "X-HTTP-Method-Override");
            });
        return http.build();
    }
}
```

2. 如果使用跨域请求，需要在 CORS 配置中允许该头：

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
            .allowedHeaders("X-HTTP-Method-Override")
            .allowCredentials(true);
    }
}
```

这样配置后，你的 Spring Boot 应用就能够支持通过 POST 请求模拟 PUT、PATCH 和 DELETE 方法了。这对于不支持这些 HTTP 方法的旧版浏览器或防火墙环境特别有用。