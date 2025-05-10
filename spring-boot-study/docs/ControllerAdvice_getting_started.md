@RestControllerAdvice 和 @ControllerAdvice 的主要区别在于 @RestControllerAdvice 相当于 @ControllerAdvice + @ResponseBody。

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ControllerAdvice {
    // 注解定义
}
```

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ControllerAdvice
@ResponseBody
public @interface RestControllerAdvice {
    // 注解定义
}
```

基本区别示例：

```java
// 传统的 @ControllerAdvice 用法
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("error", e.getMessage());
        mav.setViewName("error");
        return mav;
    }
}

// @RestControllerAdvice 用法
@RestControllerAdvice
public class GlobalRestExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse error = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

REST API 错误处理：

```java
@RestControllerAdvice
public class RestApiExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFound(ResourceNotFoundException ex) {
        return new ErrorResponse(
            "Resource not found",
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value()
        );
    }
    
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(ValidationException ex) {
        return new ErrorResponse(
            "Validation failed",
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value()
        );
    }
}
```

MVC 错误处理：

```java
@ControllerAdvice
public class MvcExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(Exception ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("message", ex.getMessage());
        mav.setViewName("error/500");
        return mav;
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDenied(AccessDeniedException ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.setViewName("error/403");
        return mav;
    }
}
```

组合使用示例：

```java
// 用于 API 的异常处理
@RestControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler {
    // API 相关的异常处理
}

// 用于 MVC 的异常处理
@ControllerAdvice(annotations = Controller.class)
public class MvcExceptionHandler {
    // MVC 相关的异常处理
}
```

特定包或类的异常处理：

```java
@RestControllerAdvice(basePackages = "com.example.api")
public class ApiExceptionHandler {
    
    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleApiException(ApiException ex) {
        return new ErrorResponse(ex.getErrorCode(), ex.getMessage());
    }
}
```

请求参数验证：

```java
@RestControllerAdvice
public class RequestValidationHandler {
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMissingParams(
            MissingServletRequestParameterException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("parameterName", ex.getParameterName());
        error.put("message", "Parameter is missing");
        return error;
    }
}
```

数据绑定和验证：

```java
@RestControllerAdvice
public class ValidationHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
```

安全异常处理：

```java
@RestControllerAdvice
public class SecurityExceptionHandler {
    
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDenied(AccessDeniedException ex) {
        return new ErrorResponse(
            "Access Denied",
            "You don't have permission to access this resource",
            HttpStatus.FORBIDDEN.value()
        );
    }
}
```

异常处理机制：

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception ex) {
        // 异常处理逻辑
        return new ErrorResponse(ex.getMessage());
    }
}

```

自定义响应格式：

```java
@RestControllerAdvice
public class CustomResponseHandler {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleAllExceptions(Exception ex) {
        ApiResponse response = ApiResponse.builder()
            .status("error")
            .message(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .build();
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

响应体处理：

```java
@RestControllerAdvice
public class ResponseBodyHandler implements ResponseBodyAdvice<Object> {
    
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
    
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, 
            MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response) {
        // 处理响应体
        if (body instanceof String) {
            // 特殊处理字符串类型
            return JsonUtils.toJson(new ResponseWrapper(body));
        }
        return new ResponseWrapper(body);
    }
}
```

异常处理链：

```java
@RestControllerAdvice
public class ChainedExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request) {
        // 1. 记录日志
        logError(ex);
        
        // 2. 构建错误响应
        ErrorResponse error = buildErrorResponse(ex);
        
        // 3. 发送通知
        notifyAdmin(ex);
        
        // 4. 返回响应
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

条件处理：

```java
@RestControllerAdvice
public class ConditionalExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex, WebRequest request) {
        // 根据不同条件返回不同响应
        if (isApiRequest(request)) {
            return handleApiException(ex);
        } else {
            return handleWebException(ex);
        }
    }
    
    private boolean isApiRequest(WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        return path.startsWith("/api/");
    }
}
```

异常转换：

```java
@RestControllerAdvice
public class ExceptionTranslator {
    
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponse> handleSQLException(SQLException ex) {
        // 将SQL异常转换为API友好的响应
        DatabaseError error = DatabaseError.fromSQLException(ex);
        return new ResponseEntity<>(error.toResponse(), HttpStatus.BAD_REQUEST);
    }
}

```

上下文感知处理：

```java
@RestControllerAdvice
public class ContextAwareExceptionHandler {
    
    @Autowired
    private HttpServletRequest request;
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse error = ErrorResponse.builder()
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .timestamp(LocalDateTime.now())
            .build();
            
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

全局数据绑定：

```java
@ControllerAdvice
public class GlobalModelAttributes {
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 自定义数据绑定
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
    
    @ModelAttribute
    public void addAttributes(Model model) {
        // 添加全局属性
        model.addAttribute("timestamp", new Date());
        model.addAttribute("globalMessage", "Welcome to our site");
    }
}
```

应用场景：

@RestControllerAdvice 适用于：

- REST API 应用
- 返回 JSON/XML 格式的响应
- 微服务架构
- 前后端分离的应用

@ControllerAdvice 适用于：

- 传统 Web MVC 应用
- 需要返回视图的应用
- 服务器端渲染的应用
- 混合型 Web 应用

使用建议：

- 如果是纯 REST API，使用 @RestControllerAdvice
- 如果是传统 MVC，使用 @ControllerAdvice
- 如果是混合应用，可以两者都用

最佳实践：

- 合理规划异常处理层级，注意异常处理的粒度，避免过重的异常处理逻辑
- 提供清晰的错误信息，避免暴露敏感信息，可以使用合理的错误码设计
- 合理使用日志级别，完善的日志记录
- 保持响应格式的一致性，统一异常响应格式

