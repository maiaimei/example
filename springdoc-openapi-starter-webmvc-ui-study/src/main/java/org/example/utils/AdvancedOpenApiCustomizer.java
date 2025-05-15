package org.example.utils;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.DateSchema;
import io.swagger.v3.oas.models.media.DateTimeSchema;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

/**
 * Customizes the OpenAPI documentation by dynamically generating schemas and updating operation responses.
 */
@Component
public class AdvancedOpenApiCustomizer {

  @Autowired
  private WebApplicationContext webApplicationContext;

  /**
   * Customizes the provided OpenAPI object by generating schemas and updating operation responses.
   *
   * @param openAPI the OpenAPI object to customize
   */
  public void customise(OpenAPI openAPI) {
    OpenAPIModelSchemaGenerator generator = new OpenAPIModelSchemaGenerator(openAPI);
    generator.generateSchemas("org.example.model");

    RequestMappingHandlerMapping mappingHandler = webApplicationContext.getBean(RequestMappingHandlerMapping.class);
    Map<RequestMappingInfo, HandlerMethod> handlerMethods = mappingHandler.getHandlerMethods();
    Map<OperationId, HandlerMethod> operationMethods = collectOperationMethods(handlerMethods);

    // 更新OpenAPI文档
    final Paths paths = openAPI.getPaths();
    if (paths != null) {
      paths.forEach((pathUrl, pathItem) -> {
        processOperation(pathItem.getGet(), "get", pathUrl, operationMethods, generator);
        processOperation(pathItem.getPost(), "post", pathUrl, operationMethods, generator);
        processOperation(pathItem.getPut(), "put", pathUrl, operationMethods, generator);
        processOperation(pathItem.getDelete(), "delete", pathUrl, operationMethods, generator);
        processOperation(pathItem.getPatch(), "patch", pathUrl, operationMethods, generator);
      });
    }
  }

  /**
   * Collects all operation methods from the handler methods.
   *
   * @param handlerMethods the map of RequestMappingInfo to HandlerMethod
   * @return a map of OperationId to HandlerMethod
   */
  private Map<OperationId, HandlerMethod> collectOperationMethods(Map<RequestMappingInfo, HandlerMethod> handlerMethods) {
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
          operationMethods.put(new OperationId(pattern, method.name().toLowerCase()), handlerMethod);
        }
      }
    }
    return operationMethods;
  }

  /**
   * Extracts the actual return type of a method parameter, handling generic types like ResponseEntity.
   *
   * @param returnParameter the method parameter to analyze
   * @return the actual return type
   */
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

  /**
   * Updates the operation response schema and example based on the actual return type.
   *
   * @param operation  the operation to update
   * @param actualType the actual return type of the operation
   * @param generator  the schema generator
   */
  private void updateOperationResponse(Operation operation, Type actualType, OpenAPIModelSchemaGenerator generator) {
    ApiResponse successResponse = operation.getResponses().computeIfAbsent("200", k -> new ApiResponse());

    Content content = new Content();
    MediaType mediaType = new MediaType();

    // 创建包装响应schema
    Schema<?> wrapperSchema = new ObjectSchema()
        .type("object");

    // 获取基础响应示例
    Map<String, Object> exampleValues = new HashMap<>(generator.getExamples().get(SuccessResponse.class));

    // 处理实际返回类型
    Schema<?> dataSchema = createResponseSchema(actualType, generator);
    Object dataExample = generateDataExample(dataSchema, actualType, generator);

    // 更新示例中的 data 字段
    exampleValues.put("data", dataExample);

    // 设置其他属性的 schema
    exampleValues.forEach((key, val) -> {
      if (!key.equals("data")) {
        Schema<?> valSchema = createPrimitiveSchema(val.getClass()).example(val);
        wrapperSchema.addProperty(key, valSchema);
      }
    });

    // 添加 data 属性到 wrapperSchema
    wrapperSchema.addProperty("data", dataSchema);

    // 设置整个响应的示例
    mediaType.setSchema(wrapperSchema);
    mediaType.setExample(exampleValues);

    content.addMediaType("application/json", mediaType);
    successResponse.setContent(content);
  }

  /**
   * Generates an example for the data field based on the schema and type.
   *
   * @param dataSchema the schema of the data field
   * @param actualType the actual type of the data field
   * @param generator  the schema generator
   * @return the generated example
   */
  private Object generateDataExample(Schema<?> dataSchema, Type actualType, OpenAPIModelSchemaGenerator generator) {
    if (dataSchema instanceof ArraySchema arraySchema) {
      // 处理数组类型
      List<Object> exampleArray = new ArrayList<>();
      Schema<?> itemSchema = arraySchema.getItems();
      Object itemExample = itemSchema.getExample();
      if (itemExample != null) {
        exampleArray.add(itemExample);
      } else {
        // 如果数组项没有示例，创建默认示例
        exampleArray.add(createExampleForArrayItem(itemSchema, generator));
      }
      return exampleArray;
    } else if (dataSchema instanceof ObjectSchema) {
      // 处理对象类型
      if (dataSchema.get$ref() != null) {
        // 处理引用类型
        return generator.getExamples().get(actualType.getClass());
      } else {
        // 处理普通对象
        return dataSchema.getExample();
      }
    } else {
      // 处理基本数据类型
      return Optional.ofNullable(dataSchema.getExample()).orElse(getDefaultExampleForSchema(dataSchema));
    }
  }

  private Object getDefaultExampleForSchema(Schema<?> schema) {
    String type = schema.getType();
    if (type == null) {
      return null;
    }

    return switch (type) {
      case "string" -> "example";
      case "integer" -> 1;
      case "number" -> {
        if ("float".equals(schema.getFormat())) {
          yield 1.0f;
        }
        yield 1.0;
      }
      case "boolean" -> true;
      case "object" -> new HashMap<>();
      case "array" -> new ArrayList<>();
      default -> null;
    };
  }

  private Schema<?> createPrimitiveSchema(Class<?> type) {
    if (String.class.equals(type)) {
      return new StringSchema();
    } else if (Integer.class.equals(type) || int.class.equals(type)) {
      return new IntegerSchema();
    } else if (Long.class.equals(type) || long.class.equals(type)) {
      return new IntegerSchema().format("int64");
    } else if (Float.class.equals(type) || float.class.equals(type)) {
      return new NumberSchema().format("float");
    } else if (Double.class.equals(type) || double.class.equals(type)) {
      return new NumberSchema().format("double");
    } else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
      return new BooleanSchema();
    } else if (BigDecimal.class.equals(type)) {
      return new NumberSchema();
    } else if (Date.class.equals(type)) {
      return new DateTimeSchema();
    } else if (LocalDateTime.class.equals(type)) {
      return new DateTimeSchema();
    } else if (LocalDate.class.equals(type)) {
      return new DateSchema();
    }
    return new ObjectSchema();
  }

  private Object createExampleForArrayItem(Schema<?> itemSchema, OpenAPIModelSchemaGenerator generator) {
    // 优先使用已有的示例
    if (itemSchema.getExample() != null) {
      return itemSchema.getExample();
    }

    // 处理引用类型
    if (itemSchema.get$ref() != null) {
      String className = itemSchema.get$ref().substring(itemSchema.get$ref().lastIndexOf("/") + 1);
      Map<String, Object> example = generator.getExamples().get(className);
      if (example != null) {
        return example;
      }
    }

    // 返回默认示例
    return getDefaultExampleForSchema(itemSchema);
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
      final Map<String, Object> map = generator.getExamples().get(clazz);
      return new Schema<>().$ref("#/components/schemas/" + clazz.getSimpleName()).example(map);
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
    // 明确设置类型为array
    arraySchema.setType("array");
    // 设置数组项的schema
    arraySchema.setItems(createResponseSchema(elementType, generator));
    // 可选：设置一些描述信息
    arraySchema.setDescription("Array of " + elementType.getTypeName());
    // 可选：设置默认值为空数组
    arraySchema.setDefault(Collections.emptyList());

    return arraySchema;
  }


  private Schema<?> createMapResponseSchema(Type[] typeArguments, OpenAPIModelSchemaGenerator generator) {
    MapSchema mapSchema = new MapSchema();
    if (typeArguments.length > 1) {
      mapSchema.setAdditionalProperties(createResponseSchema(typeArguments[1], generator));
    }
    return mapSchema;
  }

  /**
   * Processes an operation by updating its response schema and example.
   *
   * @param operation        the operation to process
   * @param httpMethod       the HTTP method of the operation
   * @param pathUrl          the URL path of the operation
   * @param operationMethods the map of operation IDs to handler methods
   * @param generator        the schema generator
   */
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
