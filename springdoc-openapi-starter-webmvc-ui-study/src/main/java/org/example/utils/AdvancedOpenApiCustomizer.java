package org.example.utils;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class AdvancedOpenApiCustomizer {

  @Autowired
  private WebApplicationContext webApplicationContext;

  public void customise(OpenAPI openAPI) {
    OpenAPIModelSchemaGenerator generator = new OpenAPIModelSchemaGenerator(openAPI);
    generator.generateSchemas("org.example.model");

    // 获取所有的RequestMapping方法
    RequestMappingHandlerMapping mappingHandler = webApplicationContext
        .getBean(RequestMappingHandlerMapping.class);

    Map<RequestMappingInfo, HandlerMethod> handlerMethods = mappingHandler.getHandlerMethods();
    Map<OperationId, HandlerMethod> operationMethods = new HashMap<>();

    // 收集所有Controller的方法信息
    for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
      RequestMappingInfo mappingInfo = entry.getKey();
      HandlerMethod handlerMethod = entry.getValue();

      // 获取路径和HTTP方法
      Set<String> patterns = mappingInfo.getPatternValues();
      Set<RequestMethod> methods = mappingInfo.getMethodsCondition().getMethods();

      for (String pattern : patterns) {
        for (RequestMethod method : methods) {
          operationMethods.put(
              new OperationId(pattern, method.name().toLowerCase()),
              handlerMethod
          );
        }
      }
    }

    // 更新OpenAPI文档
    final Paths paths = openAPI.getPaths();
    if (paths != null) {
      paths.forEach((pathUrl, pathItem) -> {
        // 处理每种HTTP方法
        processOperation(pathItem.getGet(), "get", pathUrl, operationMethods, generator);
        processOperation(pathItem.getPost(), "post", pathUrl, operationMethods, generator);
        processOperation(pathItem.getPut(), "put", pathUrl, operationMethods, generator);
        processOperation(pathItem.getDelete(), "delete", pathUrl, operationMethods, generator);
        processOperation(pathItem.getPatch(), "patch", pathUrl, operationMethods, generator);
      });
    }
  }

  private Type extractActualReturnType(MethodParameter returnParameter) {
    // 获取方法返回类型
    Type returnType = returnParameter.getGenericParameterType();

    // 处理ResponseEntity
    if (returnType instanceof ParameterizedType parameterizedType) {
      Class<?> rawType = (Class<?>) parameterizedType.getRawType();
      if (ResponseEntity.class.isAssignableFrom(rawType)) {
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        if (typeArguments.length > 0) {
          return typeArguments[0];
        }
      }
    }

    return returnType;
  }

  private void updateOperationResponse(Operation operation, Type actualType, OpenAPIModelSchemaGenerator generator) {
    ApiResponse successResponse = operation.getResponses().get("200");
    if (successResponse == null) {
      successResponse = new ApiResponse();
      operation.getResponses().addApiResponse("200", successResponse);
    }

    // 创建响应内容
    Content content = new Content();
    MediaType mediaType = new MediaType();

    // 创建包装响应schema
    Schema<?> wrapperSchema = new ObjectSchema()
        .type("object")
        .addProperties("code", new IntegerSchema().example(200))
        .addProperties("message", new StringSchema().example("操作成功"));

    // 处理实际返回类型
    Schema<?> dataSchema = createResponseSchema(actualType, generator);
    wrapperSchema.addProperties("data", dataSchema);

    mediaType.setSchema(wrapperSchema);
    content.addMediaType("application/json", mediaType);
    successResponse.setContent(content);
  }

  private Schema<?> createResponseSchema(Type type, OpenAPIModelSchemaGenerator generator) {
    if (type instanceof ParameterizedType parameterizedType) {
      Class<?> rawType = (Class<?>) parameterizedType.getRawType();
      Type[] typeArguments = parameterizedType.getActualTypeArguments();

      // 处理分页响应
      if (Page.class.isAssignableFrom(rawType)) {
        return createPageResponseSchema(typeArguments[0], generator);
      }

      // 处理集合类型
      if (Collection.class.isAssignableFrom(rawType)) {
        return createCollectionSchema(typeArguments[0], generator);
      }

      // 处理Map类型
      if (Map.class.isAssignableFrom(rawType)) {
        return createMapResponseSchema(typeArguments, generator);
      }
    }

    // 处理普通类型
    if (type instanceof Class<?>) {
      Class<?> clazz = (Class<?>) type;
      if (clazz.isPrimitive() || clazz == String.class || Number.class.isAssignableFrom(clazz)) {
        return createPrimitiveSchema(clazz);
      }

      // 处理复杂对象
      generator.processClass(clazz);
      return new Schema<>().$ref("#/components/schemas/" + clazz.getSimpleName());
    }

    return new ObjectSchema();
  }

  private Schema<?> createPageResponseSchema(Type elementType, OpenAPIModelSchemaGenerator generator) {
    ObjectSchema pageSchema = new ObjectSchema();
    pageSchema.addProperties("total", new IntegerSchema().format("int64"));
    pageSchema.addProperties("size", new IntegerSchema());
    pageSchema.addProperties("current", new IntegerSchema());
    pageSchema.addProperties("pages", new IntegerSchema());

    // 创建内容列表schema
    ArraySchema contentSchema = new ArraySchema();
    contentSchema.setItems(createResponseSchema(elementType, generator));
    pageSchema.addProperties("records", contentSchema);

    return pageSchema;
  }

  private Schema<?> createCollectionSchema(Type elementType, OpenAPIModelSchemaGenerator generator) {
    ArraySchema arraySchema = new ArraySchema();
    arraySchema.setItems(createResponseSchema(elementType, generator));
    return arraySchema;
  }

  private Schema<?> createMapResponseSchema(Type[] typeArguments, OpenAPIModelSchemaGenerator generator) {
    MapSchema mapSchema = new MapSchema();
    if (typeArguments.length > 1) {
      mapSchema.setAdditionalProperties(createResponseSchema(typeArguments[1], generator));
    }
    return mapSchema;
  }

  private Schema<?> createPrimitiveSchema(Class<?> clazz) {
    if (clazz == String.class) {
      return new StringSchema();
    } else if (clazz == Integer.class || clazz == int.class) {
      return new IntegerSchema();
    } else if (clazz == Long.class || clazz == long.class) {
      return new IntegerSchema().format("int64");
    } else if (clazz == Double.class || clazz == double.class) {
      return new NumberSchema().format("double");
    } else if (clazz == Float.class || clazz == float.class) {
      return new NumberSchema().format("float");
    } else if (clazz == Boolean.class || clazz == boolean.class) {
      return new BooleanSchema();
    } else if (clazz == BigDecimal.class) {
      return new NumberSchema();
    }
    return new ObjectSchema();
  }

  private void processOperation(Operation operation, String httpMethod, String pathUrl,
      Map<OperationId, HandlerMethod> operationMethods,
      OpenAPIModelSchemaGenerator generator) {
    if (operation != null) {
      // 标准化路径，移除路径参数的花括号
      String normalizedPath = normalizePath(pathUrl);
      HandlerMethod handlerMethod = operationMethods.get(new OperationId(normalizedPath, httpMethod));

      if (handlerMethod != null) {
        MethodParameter returnParameter = handlerMethod.getReturnType();
        // 获取实际返回类型
        Type returnType = extractActualReturnType(returnParameter);
        // 更新响应schema
        updateOperationResponse(operation, returnType, generator);
      }
    }
  }

  @Data
  @AllArgsConstructor
  private static class OperationId {

    private String path;
    private String method;
  }

  private String getMethodPath(Method method, String basePath) {
    String methodPath = null;

    // 检查各种Mapping注解
    if (method.isAnnotationPresent(GetMapping.class)) {
      methodPath = method.getAnnotation(GetMapping.class).value().length > 0
          ? method.getAnnotation(GetMapping.class).value()[0] : "";
    } else if (method.isAnnotationPresent(PostMapping.class)) {
      methodPath = method.getAnnotation(PostMapping.class).value().length > 0
          ? method.getAnnotation(PostMapping.class).value()[0] : "";
    } else if (method.isAnnotationPresent(PutMapping.class)) {
      methodPath = method.getAnnotation(PutMapping.class).value().length > 0
          ? method.getAnnotation(PutMapping.class).value()[0] : "";
    } else if (method.isAnnotationPresent(DeleteMapping.class)) {
      methodPath = method.getAnnotation(DeleteMapping.class).value().length > 0
          ? method.getAnnotation(DeleteMapping.class).value()[0] : "";
    } else if (method.isAnnotationPresent(PatchMapping.class)) {
      methodPath = method.getAnnotation(PatchMapping.class).value().length > 0
          ? method.getAnnotation(PatchMapping.class).value()[0] : "";
    } else if (method.isAnnotationPresent(RequestMapping.class)) {
      methodPath = method.getAnnotation(RequestMapping.class).value().length > 0
          ? method.getAnnotation(RequestMapping.class).value()[0] : "";
    }

    if (methodPath != null) {
      return (basePath + methodPath).replaceAll("//", "/");
    }
    return null;
  }

  private String getHttpMethod(Method method) {
    if (method.isAnnotationPresent(GetMapping.class)) {
      return "get";
    } else if (method.isAnnotationPresent(PostMapping.class)) {
      return "post";
    } else if (method.isAnnotationPresent(PutMapping.class)) {
      return "put";
    } else if (method.isAnnotationPresent(DeleteMapping.class)) {
      return "delete";
    } else if (method.isAnnotationPresent(PatchMapping.class)) {
      return "patch";
    } else if (method.isAnnotationPresent(RequestMapping.class)) {
      RequestMapping annotation = method.getAnnotation(RequestMapping.class);
      if (annotation.method().length > 0) {
        return annotation.method()[0].toString().toLowerCase();
      }
    }
    return null;
  }

  private String normalizePath(String path) {
    // 移除路径中的花括号参数，统一格式
    return path.replaceAll("\\{[^}]+\\}", "*");
  }
}
