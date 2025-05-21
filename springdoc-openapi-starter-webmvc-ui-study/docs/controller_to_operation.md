springdoc-openapi 中 Controller 方法转换为 OpenAPI Operation 的核心原理和关键类：

核心原理：

1. 方法扫描：扫描 Controller 类中的方法及其注解
2. 参数解析：解析方法参数、类型和注解
3. 返回值处理：处理方法返回值类型
4. 注解处理：解析 @Operation、@Parameter 等注解
5. 生成 Operation：构建完整的 OpenAPI Operation 对象

关键类和其作用：

1. `OpenAPIBuilder`

```java
public class OpenAPIBuilder {
    // 负责构建整个 OpenAPI 文档
    private OpenAPI openAPI;
    
    public OpenAPI build() {
        // 构建 paths
        // 构建 components
        // 构建 security schemes
    }
}
```

2. `OperationBuilder`

```java
public class OperationBuilder {
    // 构建单个 Operation
    public Operation build(HandlerMethod handlerMethod, 
                         RequestMethod requestMethod,
                         Operation operation) {
        // 处理方法注解
        // 处理参数
        // 处理返回值
        // 处理安全配置
    }
}
```

3.  `SpringDocAnnotationsUtils`

```java
public class SpringDocAnnotationsUtils {
    // 处理各种注解
    public static Operation mergeOperation(Operation operation, 
                                        io.swagger.v3.oas.annotations.Operation apiOperation) {
        // 合并注解信息到 Operation
    }
}
```

4. `RequestBuilder`

```java
public class RequestBuilder {
    // 构建请求参数
    public static Parameter buildParameter(ParameterInfo parameterInfo) {
        // 处理路径参数
        // 处理查询参数
        // 处理请求头
        // 处理 Cookie
    }
}
```

5.  `GenericResponseBuilder`

```java
public class GenericResponseBuilder {
    // 构建响应
    public ApiResponse build(ReturnType returnType) {
        // 处理返回类型
        // 处理响应状态
        // 处理响应头
    }
}
```

示例代码展示转换过程：

```java
// 1. Controller 示例
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Operation(summary = "获取用户信息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "成功"),
        @ApiResponse(responseCode = "404", description = "用户不存在")
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(
            @Parameter(description = "用户ID") 
            @PathVariable Long id) {
        // 实现逻辑
    }
}

// 2. 核心处理流程示例
public class OperationProcessor {
    
    public Operation processMethod(HandlerMethod handlerMethod) {
        // 创建基础 Operation
        Operation operation = new Operation();
        
        // 处理方法级注解
        processMethodAnnotations(handlerMethod, operation);
        
        // 处理参数
        processParameters(handlerMethod, operation);
        
        // 处理返回值
        processResponse(handlerMethod, operation);
        
        return operation;
    }
    
    private void processMethodAnnotations(HandlerMethod handlerMethod, Operation operation) {
        // 处理 @Operation 注解
        io.swagger.v3.oas.annotations.Operation operationAnn = 
            handlerMethod.getMethodAnnotation(io.swagger.v3.oas.annotations.Operation.class);
        if (operationAnn != null) {
            operation.summary(operationAnn.summary())
                    .description(operationAnn.description());
        }
        
        // 处理其他注解
    }
    
    private void processParameters(HandlerMethod handlerMethod, Operation operation) {
        Parameter[] parameters = handlerMethod.getMethodParameters();
        for (Parameter parameter : parameters) {
            // 处理参数注解
            // 构建参数描述
            // 添加到 operation
        }
    }
    
    private void processResponse(HandlerMethod handlerMethod, Operation operation) {
        // 处理返回值类型
        // 处理响应注解
        // 构建响应描述
    }
}
```

转换过程中的关键处理步骤：

1. 注解处理：

```java
public class AnnotationProcessor {
    public void processOperationAnnotation(Operation operation, Method method) {
        io.swagger.v3.oas.annotations.Operation operationAnn = 
            method.getAnnotation(io.swagger.v3.oas.annotations.Operation.class);
            
        if (operationAnn != null) {
            operation.setSummary(operationAnn.summary());
            operation.setDescription(operationAnn.description());
            operation.setDeprecated(operationAnn.deprecated());
            // 处理其他属性
        }
    }
}
```

2. 参数处理：

```java
public class ParameterProcessor {
    public List<Parameter> processMethodParameters(HandlerMethod handlerMethod) {
        List<Parameter> parameters = new ArrayList<>();
        
        for (MethodParameter methodParameter : handlerMethod.getMethodParameters()) {
            // 处理路径变量
            if (methodParameter.hasParameterAnnotation(PathVariable.class)) {
                parameters.add(createPathParameter(methodParameter));
            }
            // 处理请求参数
            else if (methodParameter.hasParameterAnnotation(RequestParam.class)) {
                parameters.add(createQueryParameter(methodParameter));
            }
            // 处理请求体
            else if (methodParameter.hasParameterAnnotation(RequestBody.class)) {
                operation.setRequestBody(createRequestBody(methodParameter));
            }
        }
        
        return parameters;
    }
}
```

3. 响应处理：

```java
public class ResponseProcessor {
    public ApiResponses processMethodResponse(HandlerMethod handlerMethod) {
        ApiResponses responses = new ApiResponses();
        
        // 处理方法返回值
        ReturnType returnType = ReturnType.of(handlerMethod);
        
        // 处理响应注解
        ApiResponse[] responseAnnotations = 
            handlerMethod.getMethodAnnotation(ApiResponses.class);
            
        if (responseAnnotations != null) {
            for (ApiResponse response : responseAnnotations) {
                responses.addApiResponse(
                    response.responseCode(), 
                    createApiResponse(response)
                );
            }
        }
        
        return responses;
    }
}
```

4. 安全处理：

```java
public class SecurityProcessor {
    public SecurityRequirement processMethodSecurity(HandlerMethod handlerMethod) {
        SecurityRequirement requirement = new SecurityRequirement();
        
        // 处理安全注解
        SecurityRequirement[] requirements = 
            handlerMethod.getMethodAnnotation(SecurityRequirements.class);
            
        if (requirements != null) {
            // 处理安全需求
        }
        
        return requirement;
    }
}
```

这个转换机制使得我们可以通过标准的 Spring MVC 注解和 OpenAPI 注解来自动生成符合 OpenAPI 规范的接口文档，大大提高了 API 文档的维护效率和准确性。