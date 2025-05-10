@RestControllerAdvice 注解主要由 Spring MVC 的 RequestMappingHandlerAdapter 和相关的组件来解析处理。

1. 核心解析类：

```java
// ControllerAdviceBean 是主要的处理类
public class ControllerAdviceBean implements OrderedRequestCallback {
    
    // 用于查找和排序 @ControllerAdvice 注解的 beans
    public static List<ControllerAdviceBean> findAnnotatedBeans(ApplicationContext context) {
        List<ControllerAdviceBean> beans = new ArrayList<>();
        for (String name : context.getBeanDefinitionNames()) {
            Object bean = context.getBean(name);
            if (hasControllerAdvice(bean)) {
                beans.add(new ControllerAdviceBean(name, bean));
            }
        }
        return beans;
    }
}
```

2. 异常处理器解析：

```java
// ExceptionHandlerExceptionResolver 处理 @ExceptionHandler 注解
public class ExceptionHandlerExceptionResolver extends AbstractHandlerMethodExceptionResolver {
    
    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request,
            HttpServletResponse response, HandlerMethod handlerMethod, Exception exception) {
        
        // 查找适合的异常处理方法
        ServletInvocableHandlerMethod exceptionHandlerMethod = getExceptionHandlerMethod(handlerMethod, exception);
        
        if (exceptionHandlerMethod != null) {
            // 执行异常处理方法
            exceptionHandlerMethod.invokeAndHandle(webRequest, mavContainer);
        }
    }
}
```

3. 初始化过程：

```java
// RequestMappingHandlerAdapter 的初始化
public class RequestMappingHandlerAdapter implements InitializingBean {
    
    @Override
    public void afterPropertiesSet() {
        // 初始化 ControllerAdvice
        initControllerAdviceCache();
        
        // 初始化其他组件
        if (this.argumentResolvers == null) {
            List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
            this.argumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
        }
    }
}
```

4. 注解扫描：

```java
// ControllerAdviceBean 的扫描过程
public class ControllerAdviceBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        // 扫描带有 @ControllerAdvice 注解的类
        Map<String, Object> beans = beanFactory.getBeansWithAnnotation(ControllerAdvice.class);
        
        for (Object bean : beans.values()) {
            // 处理每个 ControllerAdvice bean
            processControllerAdviceBean(bean);
        }
    }
}
```

5. 异常处理方法缓存：

```java
// ExceptionHandlerMethodResolver 用于缓存异常处理方法
public class ExceptionHandlerMethodResolver {
    
    private final Map<Class<? extends Throwable>, Method> mappedMethods = new HashMap<>();
    
    public ExceptionHandlerMethodResolver(Class<?> handlerType) {
        // 扫描类中的 @ExceptionHandler 方法
        for (Method method : MethodIntrospector.selectMethods(handlerType, EXCEPTION_HANDLER_METHODS)) {
            for (Class<? extends Throwable> exceptionType : getExceptionTypes(method)) {
                addExceptionMapping(exceptionType, method);
            }
        }
    }
}
```

6. 响应体处理：

```java
// RequestResponseBodyMethodProcessor 处理响应体
public class RequestResponseBodyMethodProcessor implements HandlerMethodReturnValueHandler {
    
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
            ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
            
        // 处理返回值
        mavContainer.setRequestHandled(true);
        
        // 转换响应体
        writeWithMessageConverters(returnValue, returnType, webRequest);
    }
}
```

6. 处理器适配器：

```java
// AbstractHandlerMethodAdapter 处理请求
public abstract class AbstractHandlerMethodAdapter implements HandlerAdapter {
    
    @Override
    public final ModelAndView handle(HttpServletRequest request, HttpServletResponse response, 
            Object handler) throws Exception {
            
        return handleInternal(request, response, (HandlerMethod) handler);
    }
}
```

7. 异常解析链：

```java
// HandlerExceptionResolverComposite 组合多个异常解析器
public class HandlerExceptionResolverComposite implements HandlerExceptionResolver {
    
    private List<HandlerExceptionResolver> resolvers = new ArrayList<>();
    
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) {
            
        // 遍历所有解析器尝试处理异常
        for (HandlerExceptionResolver resolver : resolvers) {
            ModelAndView mav = resolver.resolveException(request, response, handler, ex);
            if (mav != null) {
                return mav;
            }
        }
        return null;
    }
}
```

8. 方法调用处理：

```java
// ServletInvocableHandlerMethod 处理方法调用
public class ServletInvocableHandlerMethod extends InvocableHandlerMethod {
    
    public void invokeAndHandle(ServletWebRequest webRequest, ModelAndViewContainer mavContainer,
            Object... providedArgs) throws Exception {
            
        Object returnValue = invokeForRequest(webRequest, mavContainer, providedArgs);
        setResponseStatus(webRequest);
        
        if (returnValue == null) {
            if (isRequestNotModified(webRequest) || getResponseStatus() != null) {
                mavContainer.setRequestHandled(true);
                return;
            }
        }
        
        mavContainer.setRequestHandled(false);
        try {
            this.returnValueHandlers.handleReturnValue(
                    returnValue, getReturnValueType(returnValue), mavContainer, webRequest);
        }
        catch (Exception ex) {
            if (logger.isTraceEnabled()) {
                logger.trace(getReturnValueHandlingErrorMessage("Error handling return value", returnValue), ex);
            }
            throw ex;
        }
    }
}
```

9. 初始化顺序：

```java
// WebMvcConfigurationSupport 配置支持类
public class WebMvcConfigurationSupport implements ApplicationContextAware {
    
    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter adapter = createRequestMappingHandlerAdapter();
        adapter.setContentNegotiationManager(mvcContentNegotiationManager());
        adapter.setMessageConverters(getMessageConverters());
        adapter.setWebBindingInitializer(getConfigurableWebBindingInitializer());
        adapter.setCustomArgumentResolvers(getArgumentResolvers());
        adapter.setCustomReturnValueHandlers(getReturnValueHandlers());
        
        return adapter;
    }
}
```

解析流程：

1. 启动阶段：

- 扫描 @RestControllerAdvice 注解
- 创建相应的 Bean
- 初始化异常处理器

2. 请求处理流程：

- 接收请求
- 路由到相应的控制器
- 执行控制器方法
- 如果发生异常，进入异常处理流程
- 如果没有异常，进入响应处理流程

3. 异常处理流程：

- 查找匹配的异常处理器@ExceptionHandler
  * 最具体的异常处理优先
  * 同级异常按声明顺序处理
  * 默认异常处理兜底
- 执行异常处理方法
- 转换响应格式

4. 响应处理流程：

- 自动添加 @ResponseBody
- 转换响应对象为 JSON/XML
- 设置响应头和状态码

这个解析过程是 Spring MVC 框架的核心部分，涉及多个组件的协作。理解这个过程对于处理复杂的异常情况和自定义异常处理非常有帮助。