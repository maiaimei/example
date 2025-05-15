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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.model.ApiResponse.BaseResponse;
import org.example.model.ApiResponse.SuccessResponse;
import org.example.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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
    Schema<?> wrapperSchema2 = generator.getSchema(SuccessResponse.class.getSimpleName())
        .type("object")
        .$ref("#/components/schemas/" + SuccessResponse.class.getSimpleName());

    // 创建包装响应schema
    Schema<?> wrapperSchema = new ObjectSchema()
        .type("object")
        .$ref("#/components/schemas/" + SuccessResponse.class.getSimpleName());

    Map<String, Object> exampleValues = generator.getExamples().get(SuccessResponse.class);
    exampleValues.forEach((key, val) -> {
      final Class<?> valClass = val.getClass();
      if (!"data".equals(key)) {
        final Schema<?> valSchema = createPrimitiveSchema(valClass).example(val);
        wrapperSchema.addProperty(key, valSchema);
      }
    });

    // 处理实际返回类型
    Schema<?> dataSchema = createResponseSchema(actualType, generator);
    wrapperSchema.addProperty("data", dataSchema);

    mediaType.setSchema(wrapperSchema);
    content.addMediaType("application/json", mediaType);
    successResponse.setContent(content);
  }

  private Schema<?> createResponseSchema(Type type, OpenAPIModelSchemaGenerator generator) {
    if (type instanceof ParameterizedType parameterizedType) {
      Class<?> rawType = (Class<?>) parameterizedType.getRawType();
      Type[] typeArguments = parameterizedType.getActualTypeArguments();

      // 处理BaseResponse类型
      if (BaseResponse.class.equals(rawType)) {
        return createResponseSchema(typeArguments[0], generator);
      }

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
    if (type instanceof Class<?> clazz) {
      if (isPrimitiveType(clazz)) {
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
    pageSchema.addProperty("total", new IntegerSchema().format("int64"));
    pageSchema.addProperty("size", new IntegerSchema());
    pageSchema.addProperty("current", new IntegerSchema());
    pageSchema.addProperty("pages", new IntegerSchema());

    // 创建内容列表schema
    ArraySchema contentSchema = new ArraySchema();
    contentSchema.setItems(createResponseSchema(elementType, generator));
    pageSchema.addProperty("records", contentSchema);

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
      HandlerMethod handlerMethod = operationMethods.get(new OperationId(pathUrl, httpMethod));

      if (handlerMethod != null) {
        MethodParameter returnParameter = handlerMethod.getReturnType();
        // 获取实际返回类型
        Type returnType = extractActualReturnType(returnParameter);
        // 更新响应schema
        updateOperationResponse(operation, returnType, generator);
      }
    }
  }

  private boolean isPrimitiveType(Class<?> clazz) {
    if (clazz.isPrimitive() || clazz == String.class || Number.class.isAssignableFrom(clazz)) {
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }

  @Data
  @AllArgsConstructor
  private static class OperationId {

    private String path;
    private String method;
  }

}
