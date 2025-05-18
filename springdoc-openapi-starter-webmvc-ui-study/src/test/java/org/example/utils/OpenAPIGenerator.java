package org.example.utils;

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

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import java.lang.reflect.Method;
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
import org.example.model.ApiResponse.BaseResponse;
import org.example.model.ApiResponse.SuccessResponse;
import org.example.model.Page;
import org.example.openapi.OpenAPIModelSchemaGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class OpenAPIGenerator {

  private static OpenAPIModelSchemaGenerator modelSchemaGenerator;

  /**
   * 为指定控制器方法生成OpenAPI文档
   *
   * @param controllerClass 控制器类
   * @param methodName      方法名
   * @param paramTypes      方法参数类型
   * @return OpenAPI文档JSON字符串
   */
  public static String generateOpenAPIDoc(
      Class<?> controllerClass,
      String methodName,
      List<Class<?>> modelClasses,
      Class<?>... paramTypes) {
    try {
      // 创建OpenAPI对象
      OpenAPI openAPI = new OpenAPI();
      // 设置为 OpenAPI 3.1
      openAPI.specVersion(SpecVersion.V31);
      // 强制设置 openapi 版本为 3.1.0
      openAPI.openapi("3.1.0");

      // 设置基本信息
      Info info = new Info()
          .title("API Documentation")
          .version("1.0.0")
          .description("REST API Documentation")
          .termsOfService("http://terms.service.url")
          .license(new License()
              .name("Apache 2.0")
              .url("http://license.url"))
          .contact(new Contact()
              .name("API Support")
              .email("support@example.com"));
      openAPI.setInfo(info);

      modelSchemaGenerator = new OpenAPIModelSchemaGenerator(openAPI);
      modelSchemaGenerator.processClass(modelClasses);

      // 获取方法
      Method method = controllerClass.getDeclaredMethod(methodName, paramTypes);

      // 解析方法注解和生成文档
      Operation operation = parseMethodToOperation(method);

      // 获取请求路径和HTTP方法
      String path = getPath(controllerClass, method);
      PathItem.HttpMethod httpMethod = getHttpMethod(method);

      // 创建路径项
      PathItem pathItem = new PathItem();
      switch (httpMethod) {
        case GET -> pathItem.get(operation);
        case POST -> pathItem.post(operation);
        case PUT -> pathItem.put(operation);
        case DELETE -> pathItem.delete(operation);
        case PATCH -> pathItem.patch(operation);
      }

      // 设置路径
      Paths paths = new Paths();
      paths.addPathItem(path, pathItem);
      openAPI.setPaths(paths);

      // 转换为JSON
      return Json.pretty(openAPI);

    } catch (Exception e) {
      throw new RuntimeException("Failed to generate OpenAPI documentation", e);
    }
  }

  private static Operation parseMethodToOperation(Method method) {
    Operation operation = new Operation();

    // 解析方法级别的Operation注解
    io.swagger.v3.oas.annotations.Operation operationAnnotation =
        method.getAnnotation(io.swagger.v3.oas.annotations.Operation.class);
    if (operationAnnotation != null) {
      operation.setSummary(operationAnnotation.summary());
      operation.setDescription(operationAnnotation.description());
      operation.setOperationId(operationAnnotation.operationId());
      operation.setDeprecated(operationAnnotation.deprecated());
    }

    // 解析参数
    operation.setParameters(parseParameters(method));

    // 解析响应
    operation.setResponses(parseResponses(method));

    return operation;
  }

  private static List<Parameter> parseParameters(Method method) {
    List<Parameter> parameters = new ArrayList<>();

    // 解析方法参数
    java.lang.reflect.Parameter[] methodParams = method.getParameters();
    for (java.lang.reflect.Parameter param : methodParams) {
      // 处理路径参数
      PathVariable pathVar = param.getAnnotation(PathVariable.class);
      if (pathVar != null) {
        Parameter parameter = new Parameter()
            .name(pathVar.value().isEmpty() ? param.getName() : pathVar.value())
            .in("path")
            .required(true)
            .description("Path parameter: " + param.getName());
        parameters.add(parameter);
      }

      // 处理请求参数
      RequestParam reqParam = param.getAnnotation(RequestParam.class);
      if (reqParam != null) {
        Parameter parameter = new Parameter()
            .name(reqParam.value().isEmpty() ? param.getName() : reqParam.value())
            .in("query")
            .required(reqParam.required())
            .description("Query parameter: " + param.getName());
        parameters.add(parameter);
      }

      // 处理请求头参数
      RequestHeader headerParam = param.getAnnotation(RequestHeader.class);
      if (headerParam != null) {
        Parameter parameter = new Parameter()
            .name(headerParam.value().isEmpty() ? param.getName() : headerParam.value())
            .in("header")
            .required(headerParam.required())
            .description("Header parameter: " + param.getName());
        parameters.add(parameter);
      }
    }

    return parameters;
  }

  private static ApiResponses parseResponses(Method method) {
    ApiResponses responses = new ApiResponses();

    // 解析方法级别的ApiResponse注解
    io.swagger.v3.oas.annotations.responses.ApiResponse[] responseAnnotations =
        method.getAnnotationsByType(io.swagger.v3.oas.annotations.responses.ApiResponse.class);

    if (responseAnnotations.length > 0) {
      for (io.swagger.v3.oas.annotations.responses.ApiResponse annotation : responseAnnotations) {
        ApiResponse response = new ApiResponse()
            .description(annotation.description());
        responses.addApiResponse(annotation.responseCode(), response);
      }
    } else {
      // 添加默认200响应
      responses.addApiResponse("200", new ApiResponse().description("Successful operation"));
    }

    final Class<?> returnType = method.getReturnType();

    createErrorResponse(responses);
    //createOrUpdateSuccessResponse(responses, );

    return responses;
  }

  private static String getPath(Class<?> controllerClass, Method method) {
    StringBuilder path = new StringBuilder();

    // 获取控制器级别的路径
    RequestMapping classMapping = controllerClass.getAnnotation(RequestMapping.class);
    if (classMapping != null && classMapping.value().length > 0) {
      path.append(classMapping.value()[0]);
    }

    // 获取方法级别的路径
    String methodPath = "";
    if (method.isAnnotationPresent(GetMapping.class)) {
      GetMapping annotation = method.getAnnotation(GetMapping.class);
      methodPath = annotation.value().length > 0 ? annotation.value()[0] : "";
    } else if (method.isAnnotationPresent(PostMapping.class)) {
      PostMapping annotation = method.getAnnotation(PostMapping.class);
      methodPath = annotation.value().length > 0 ? annotation.value()[0] : "";
    } else if (method.isAnnotationPresent(PutMapping.class)) {
      PutMapping annotation = method.getAnnotation(PutMapping.class);
      methodPath = annotation.value().length > 0 ? annotation.value()[0] : "";
    } else if (method.isAnnotationPresent(DeleteMapping.class)) {
      DeleteMapping annotation = method.getAnnotation(DeleteMapping.class);
      methodPath = annotation.value().length > 0 ? annotation.value()[0] : "";
    } else if (method.isAnnotationPresent(PatchMapping.class)) {
      PatchMapping annotation = method.getAnnotation(PatchMapping.class);
      methodPath = annotation.value().length > 0 ? annotation.value()[0] : "";
    } else if (method.isAnnotationPresent(RequestMapping.class)) {
      RequestMapping annotation = method.getAnnotation(RequestMapping.class);
      methodPath = annotation.value().length > 0 ? annotation.value()[0] : "";
    }

    if (!methodPath.startsWith("/") && !methodPath.isEmpty()) {
      path.append("/");
    }
    path.append(methodPath);

    return path.toString();
  }

  private static PathItem.HttpMethod getHttpMethod(Method method) {
    if (method.isAnnotationPresent(GetMapping.class)) {
      return PathItem.HttpMethod.GET;
    } else if (method.isAnnotationPresent(PostMapping.class)) {
      return PathItem.HttpMethod.POST;
    } else if (method.isAnnotationPresent(PutMapping.class)) {
      return PathItem.HttpMethod.PUT;
    } else if (method.isAnnotationPresent(DeleteMapping.class)) {
      return PathItem.HttpMethod.DELETE;
    } else if (method.isAnnotationPresent(PatchMapping.class)) {
      return PathItem.HttpMethod.PATCH;
    } else if (method.isAnnotationPresent(RequestMapping.class)) {
      RequestMapping annotation = method.getAnnotation(RequestMapping.class);
      if (annotation.method().length > 0) {
        return PathItem.HttpMethod.valueOf(annotation.method()[0].name());
      }
    }
    return PathItem.HttpMethod.GET; // 默认GET
  }

  private static void createOrUpdateSuccessResponse(ApiResponses responses, Type actualType) {
    ApiResponse successResponse = responses.computeIfAbsent(SUCCESS_CODE, k -> new ApiResponse());

    Content content = new Content();
    MediaType mediaType = new MediaType();

    // 创建包装响应schema
    final String successResponseSimpleName = SuccessResponse.class.getSimpleName();
    final Schema<?> successResponseSchema = modelSchemaGenerator.getSchema(successResponseSimpleName);
    Schema<?> wrapperSchema = new ObjectSchema()
        .type(OBJECT)
        .name(successResponseSimpleName)
        .description(successResponseSchema.getDescription())
        .$ref(COMPONENTS_SCHEMAS_PATH + successResponseSimpleName);

    // 获取基础响应示例
    Map<String, Object> exampleValues = new HashMap<>(modelSchemaGenerator.getExample(SuccessResponse.class));

    // 处理实际返回类型
    Schema<?> dataSchema = createResponseSchema(actualType);
    Object dataExample = generateDataExample(dataSchema, actualType);

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

  private static void createErrorResponse(ApiResponses responses) {
    // 401 未授权
    createCustomErrorResponse(responses, HttpStatus.UNAUTHORIZED,
        "Invalid authentication credentials",
        "Missing or invalid Authorization header");

    // 403 禁止访问
    createCustomErrorResponse(responses, HttpStatus.FORBIDDEN,
        "Access denied",
        "User does not have permission to access this resource");

    // 500 服务器错误
    createCustomErrorResponse(responses, HttpStatus.INTERNAL_SERVER_ERROR,
        "An unexpected error occurred",
        "Unable to process request due to internal server error");
  }

  private static void createCustomErrorResponse(ApiResponses responses,
      HttpStatus httpStatus,
      String message,
      String details) {
    // 创建自定义错误响应 schema
    final Schema<?> errorSchema = new ObjectSchema()
        .addProperty("code", new IntegerSchema()
            .example(httpStatus.value())
            .description("HTTP status code"))
        .addProperty("message", new StringSchema()
            .example(message)
            .description("Error message"))
        .addProperty("error", new StringSchema()
            .example(httpStatus.getReasonPhrase())
            .description("Error type"))
        .addProperty("details", new StringSchema()
            .example(details)
            .description("Detailed error description"))
        .addProperty("timestamp", new StringSchema()
            .example(LocalDateTime.now().toString())
            .description("Error timestamp"))
        .addProperty("path", new StringSchema()
            .example("/examples")
            .description("Request path"))
        .addProperty("method", new StringSchema()
            .example("GET")
            .description("HTTP method"))
        .addProperty("correlationId", new StringSchema()
            .example("2025051522504809400001")
            .description("Unique error correlation ID"));

    // 设置 schema 描述
    errorSchema.setDescription(httpStatus.getReasonPhrase() + " Error Response");

    // 创建示例数据
    Map<String, Object> errorExample = Map.of(
        "code", httpStatus.value(),
        "message", message,
        "error", httpStatus.getReasonPhrase(),
        "details", details,
        "timestamp", LocalDateTime.now().toString(),
        "path", "/examples",
        "method", "GET",
        "correlationId", "2025051522504809400001"
    );

    // 添加到响应中
    responses.addApiResponse(String.valueOf(httpStatus.value()),
        new ApiResponse()
            .description(httpStatus.getReasonPhrase())
            .content(new Content()
                .addMediaType(APPLICATION_JSON,
                    new MediaType()
                        .schema(errorSchema)
                        .example(errorExample))));
  }

  private static Object generateDataExample(Schema<?> dataSchema, Type actualType) {
    if (dataSchema instanceof ArraySchema arraySchema) {
      // 处理数组类型
      List<Object> exampleArray = new ArrayList<>();
      Schema<?> itemSchema = arraySchema.getItems();
      Object itemExample = itemSchema.getExample();
      if (itemExample != null) {
        exampleArray.add(itemExample);
      } else {
        // 如果数组项没有示例，创建默认示例
        exampleArray.add(createExampleForArrayItem(itemSchema, actualType));
      }
      return exampleArray;
    } else if (dataSchema instanceof ObjectSchema) {
      // 处理对象类型
      if (dataSchema.get$ref() != null) {
        // 处理引用类型
        return modelSchemaGenerator.getExample(actualType.getClass());
      } else {
        // 处理普通对象
        return dataSchema.getExample();
      }
    } else {
      // 处理基本数据类型
      return Optional.ofNullable(dataSchema.getExample()).orElse(getDefaultExampleForSchema(dataSchema));
    }
  }

  private static Object createExampleForArrayItem(Schema<?> itemSchema, Type actualType) {
    // 优先使用已有的示例
    if (itemSchema.getExample() != null) {
      return itemSchema.getExample();
    }

    // 处理引用类型
    if (itemSchema.get$ref() != null) {
      Map<String, Object> example = modelSchemaGenerator.getExample(actualType.getClass());
      if (example != null) {
        return example;
      }
    }

    // 返回默认示例
    return getDefaultExampleForSchema(itemSchema);
  }

  private static Object getDefaultExampleForSchema(Schema<?> schema) {
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

  private static Schema<?> createResponseSchema(Type type) {
    if (type instanceof ParameterizedType parameterizedType) {
      Class<?> rawType = (Class<?>) parameterizedType.getRawType();
      Type[] typeArguments = parameterizedType.getActualTypeArguments();

      // 处理BaseResponse类型
      if (BaseResponse.class.equals(rawType)) {
        return createResponseSchema(typeArguments[0]);
      }

      // 处理分页响应
      if (Page.class.isAssignableFrom(rawType)) {
        return createPageResponseSchema(typeArguments[0]);
      }

      // 处理集合类型
      if (Collection.class.isAssignableFrom(rawType)) {
        return createCollectionSchema(typeArguments[0]);
      }

      // 处理Map类型
      if (Map.class.isAssignableFrom(rawType)) {
        return createMapResponseSchema(typeArguments);
      }
    }

    // 处理普通类型
    if (type instanceof Class<?> clazz) {
      if (ClassUtils.isPrimitiveOrWrapper(clazz)) {
        return createPrimitiveSchema(clazz);
      }

      // 处理复杂对象
      modelSchemaGenerator.processClass(clazz);
      final Map<String, Object> map = modelSchemaGenerator.getProcessedExamples().get(clazz);
      return new Schema<>().$ref(COMPONENTS_SCHEMAS_PATH + clazz.getSimpleName()).example(map);
    }

    return new ObjectSchema();
  }

  private static Schema<?> createPageResponseSchema(Type elementType) {
    ObjectSchema pageSchema = new ObjectSchema();
    pageSchema.addProperty("total", new IntegerSchema().format(INT64));
    pageSchema.addProperty("size", new IntegerSchema());
    pageSchema.addProperty("current", new IntegerSchema());
    pageSchema.addProperty("pages", new IntegerSchema());

    // 创建内容列表schema
    ArraySchema contentSchema = new ArraySchema();
    contentSchema.setItems(createResponseSchema(elementType));
    pageSchema.addProperty("records", contentSchema);

    return pageSchema;
  }

  private static Schema<?> createCollectionSchema(Type elementType) {
    ArraySchema arraySchema = new ArraySchema();
    // 明确设置类型为array
    arraySchema.setType(ARRAY);
    // 设置数组项的schema
    arraySchema.setItems(createResponseSchema(elementType));
    // 可选：设置一些描述信息
    arraySchema.setDescription("Array of " + elementType.getTypeName());
    // 可选：设置默认值为空数组
    arraySchema.setDefault(Collections.emptyList());

    return arraySchema;
  }

  private static Schema<?> createMapResponseSchema(Type[] typeArguments) {
    MapSchema mapSchema = new MapSchema();
    if (typeArguments.length > 1) {
      mapSchema.setAdditionalProperties(createResponseSchema(typeArguments[1]));
    }
    return mapSchema;
  }

  private static Schema<?> createPrimitiveSchema(Class<?> type) {
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

}
