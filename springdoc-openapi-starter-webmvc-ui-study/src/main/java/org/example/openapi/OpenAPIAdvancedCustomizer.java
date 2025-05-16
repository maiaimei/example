package org.example.openapi;

import static org.example.openapi.OpenAPIConstants.APPLICATION_JSON;
import static org.example.openapi.OpenAPIConstants.COMPONENTS_SCHEMAS_PATH;
import static org.example.openapi.OpenAPIConstants.DATA;
import static org.example.openapi.OpenAPIConstants.SUCCESS_CODE;
import static org.example.openapi.OpenAPIType.ARRAY;
import static org.example.openapi.OpenAPIType.BOOLEAN;
import static org.example.openapi.OpenAPIType.DOUBLE;
import static org.example.openapi.OpenAPIType.FLOAT;
import static org.example.openapi.OpenAPIType.INT64;
import static org.example.openapi.OpenAPIType.INTEGER;
import static org.example.openapi.OpenAPIType.NUMBER;
import static org.example.openapi.OpenAPIType.OBJECT;
import static org.example.openapi.OpenAPIType.STRING;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
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
import java.util.Objects;
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
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Advanced customizer for OpenAPI (Swagger) documentation. This class provides enhanced customization capabilities for OpenAPI
 * documentation, including operation ID generation, schema customization, and response handling.
 */
@Component
public class OpenAPIAdvancedCustomizer {

  @Autowired
  private WebApplicationContext webApplicationContext;

  private RequestMappingHandlerMapping mappingHandler;

  /**
   * Customizes the OpenAPI documentation by processing paths, operations, and schemas. This method is called by the OpenAPI
   * generation process to apply custom modifications.
   *
   * @param openAPI The OpenAPI object to be customized
   */
  public void customise(OpenAPI openAPI) {
    OpenAPIModelSchemaGenerator generator = new OpenAPIModelSchemaGenerator(openAPI);
    generator.generateSchemas("org.example.model");

    mappingHandler = webApplicationContext.getBean(RequestMappingHandlerMapping.class);
    Map<RequestMappingInfo, HandlerMethod> handlerMethods = mappingHandler.getHandlerMethods();
    Map<OperationId, HandlerMethod> operationMethods = collectOperationMethods(handlerMethods);

    // 更新OpenAPI文档
    final Paths paths = openAPI.getPaths();
    if (Objects.nonNull(paths)) {
      paths.forEach((pathUrl, pathItem) -> processHttpMethods(pathItem, pathUrl, operationMethods, generator));
    }
  }

  /**
   * Collects all operation methods from the handler methods.
   *
   * @param handlerMethods the map of RequestMappingInfo to HandlerMethod
   * @return a map of OperationId to HandlerMethod
   */
  private Map<OperationId, HandlerMethod> collectOperationMethods(Map<RequestMappingInfo, HandlerMethod> handlerMethods) {
    Map<OperationId, HandlerMethod> operationMethods = new HashMap<>(handlerMethods.size() * 2);
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
   * Processes HTTP methods for a given path item. Handles GET, POST, PUT, DELETE, and PATCH operations.
   *
   * @param pathItem         The path item containing operations
   * @param pathUrl          The URL path being processed
   * @param operationMethods Map of operation IDs to handler methods
   * @param generator        Schema generator for OpenAPI models
   */
  private void processHttpMethods(PathItem pathItem, String pathUrl,
      Map<OperationId, HandlerMethod> operationMethods,
      OpenAPIModelSchemaGenerator generator) {
    processOperation(pathItem.getGet(), "get", pathUrl, operationMethods, generator);
    processOperation(pathItem.getPost(), "post", pathUrl, operationMethods, generator);
    processOperation(pathItem.getPut(), "put", pathUrl, operationMethods, generator);
    processOperation(pathItem.getDelete(), "delete", pathUrl, operationMethods, generator);
    processOperation(pathItem.getPatch(), "patch", pathUrl, operationMethods, generator);
  }

  /**
   * Processes a single operation, updating its schema and response information.
   *
   * @param operation        The operation to process
   * @param httpMethod       The HTTP method
   * @param pathUrl          The URL path
   * @param operationMethods Map of operation IDs to handler methods
   * @param generator        Schema generator for OpenAPI models
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

  /**
   * Updates the response section of an operation with appropriate schemas.
   *
   * @param operation  The operation to update
   * @param actualType The actual return type of the operation
   * @param generator  Schema generator for OpenAPI models
   */
  private void updateOperationResponse(Operation operation, Type actualType, OpenAPIModelSchemaGenerator generator) {
    ApiResponse successResponse = operation.getResponses().computeIfAbsent(SUCCESS_CODE, k -> new ApiResponse());

    Content content = new Content();
    MediaType mediaType = new MediaType();

    // 创建包装响应schema
    final String successResponseSimpleName = SuccessResponse.class.getSimpleName();
    final Schema<?> successResponseSchema = generator.getSchema(successResponseSimpleName);
    Schema<?> wrapperSchema = new ObjectSchema()
        .type(OBJECT)
        .name(successResponseSimpleName)
        .description(successResponseSchema.getDescription())
        .$ref(COMPONENTS_SCHEMAS_PATH + successResponseSimpleName);

    // 获取基础响应示例
    Map<String, Object> exampleValues = new HashMap<>(generator.getExample(SuccessResponse.class));

    // 处理实际返回类型
    Schema<?> dataSchema = createResponseSchema(actualType, generator);
    Object dataExample = generateDataExample(dataSchema, actualType, generator);

    // 更新示例中的 data 字段
    exampleValues.put(DATA, dataExample);

    // 设置其他属性的 schema
    exampleValues.forEach((key, val) -> {
      if (!key.equals(DATA)) {
        Schema<?> valSchema = createPrimitiveSchema(val.getClass()).example(val);
        wrapperSchema.addProperty(key, valSchema);
      }
    });

    // 添加 data 属性到 wrapperSchema
    wrapperSchema.addProperty(DATA, dataSchema);

    // 设置整个响应的示例
    mediaType.setSchema(wrapperSchema);
    mediaType.setExample(exampleValues);

    content.addMediaType(APPLICATION_JSON, mediaType);
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
        exampleArray.add(createExampleForArrayItem(itemSchema, actualType, generator));
      }
      return exampleArray;
    } else if (dataSchema instanceof ObjectSchema) {
      // 处理对象类型
      if (dataSchema.get$ref() != null) {
        // 处理引用类型
        return generator.getExample(actualType.getClass());
      } else {
        // 处理普通对象
        return dataSchema.getExample();
      }
    } else {
      // 处理基本数据类型
      return Optional.ofNullable(dataSchema.getExample()).orElse(getDefaultExampleForSchema(dataSchema));
    }
  }

  private Object createExampleForArrayItem(Schema<?> itemSchema, Type actualType, OpenAPIModelSchemaGenerator generator) {
    // 优先使用已有的示例
    if (itemSchema.getExample() != null) {
      return itemSchema.getExample();
    }

    // 处理引用类型
    if (itemSchema.get$ref() != null) {
      Map<String, Object> example = generator.getExample(actualType.getClass());
      if (example != null) {
        return example;
      }
    }

    // 返回默认示例
    return getDefaultExampleForSchema(itemSchema);
  }

  /**
   * Generates a default example value for a given schema type.
   *
   * @param schema The schema to generate an example for
   * @return A default example value appropriate for the schema type
   */
  private Object getDefaultExampleForSchema(Schema<?> schema) {
    String type = schema.getType();
    if (type == null) {
      return null;
    }

    return switch (type) {
      case STRING -> "example";
      case INTEGER -> 1;
      case NUMBER -> {
        if (FLOAT.equals(schema.getFormat())) {
          yield 1.0f;
        }
        yield 1.0;
      }
      case BOOLEAN -> true;
      case OBJECT -> new HashMap<>();
      case ARRAY -> new ArrayList<>();
      default -> null;
    };
  }

  /**
   * Creates a response schema for the given type.
   *
   * @param type      The Java type to create a schema for
   * @param generator Schema generator for OpenAPI models
   * @return Schema representing the type
   */
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
      if (ClassUtils.isPrimitiveOrWrapper(clazz)) {
        return createPrimitiveSchema(clazz);
      }

      // 处理复杂对象
      generator.processClass(clazz);
      final Map<String, Object> map = generator.getProcessedExamples().get(clazz);
      return new Schema<>().$ref(COMPONENTS_SCHEMAS_PATH + clazz.getSimpleName()).example(map);
    }

    return new ObjectSchema();
  }

  /**
   * Creates a schema for paginated responses.
   *
   * @param elementType The type of elements in the page
   * @param generator   Schema generator for OpenAPI models
   * @return Schema representing a paginated response
   */
  private Schema<?> createPageResponseSchema(Type elementType, OpenAPIModelSchemaGenerator generator) {
    ObjectSchema pageSchema = new ObjectSchema();
    pageSchema.addProperty("total", new IntegerSchema().format(INT64));
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
    arraySchema.setType(ARRAY);
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

  private Schema<?> createPrimitiveSchema(Class<?> type) {
    if (String.class.equals(type)) {
      return new StringSchema();
    } else if (Integer.class.equals(type) || int.class.equals(type)) {
      return new IntegerSchema();
    } else if (Long.class.equals(type) || long.class.equals(type)) {
      return new IntegerSchema().format("int64");
    } else if (Float.class.equals(type) || float.class.equals(type)) {
      return new NumberSchema().format(FLOAT);
    } else if (Double.class.equals(type) || double.class.equals(type)) {
      return new NumberSchema().format(DOUBLE);
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

  @Data
  @AllArgsConstructor
  private static class OperationId {

    private String path;
    private String method;
  }

}
