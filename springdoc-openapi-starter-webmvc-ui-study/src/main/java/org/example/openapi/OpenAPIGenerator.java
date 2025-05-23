package org.example.openapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.example.model.ApiRequest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@SuppressWarnings("all")
public class OpenAPIGenerator {

  private final ObjectMapper objectMapper;

  public OpenAPIGenerator() {
    this.objectMapper = Json.mapper();
  }

  // 生成 OpenAPI 文档
  public OpenAPI generateAPI(Class<?> controllerClass, String methodName) {
    // 创建 OpenAPI 对象
    OpenAPI openAPI = new OpenAPI();
    // 设置 OpenAPI 版本
    openAPI.specVersion(SpecVersion.V31);
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

    // 设置控制器标签
    openAPI.setTags(generateControllerTags(controllerClass));

    // 查找指定方法
    Method targetMethod = findTargetMethod(controllerClass, methodName);

    // 生成路径
    Paths paths = new Paths();
    // 生成路径项
    PathItem pathItem = generatePathItem(controllerClass, targetMethod);
    // 添加路径项到路径
    if (pathItem != null && pathItem.readOperations().size() > 0) {
      String fullPath = getFullPath(controllerClass, targetMethod);
      paths.addPathItem(fullPath, pathItem);
    }
    // 设置路径
    openAPI.setPaths(paths);

    // 生成组件
    Components components = new Components();
    // 生成模式并设置到组件
    components.setSchemas(generateSchemas(targetMethod));
    // 设置组件
    openAPI.setComponents(components);

    return openAPI;
  }

  // 生成控制器标签
  private List<Tag> generateControllerTags(Class<?> controllerClass) {
    List<Tag> tags = new ArrayList<>();

    // 获取类级别的@Tag注解
    io.swagger.v3.oas.annotations.tags.Tag controllerTag = controllerClass.getAnnotation(
        io.swagger.v3.oas.annotations.tags.Tag.class);
    // 如果有@Tag注解，使用注解中的名称和描述
    if (controllerTag != null) {
      tags.add(new Tag()
          .name(controllerTag.name())
          .description(controllerTag.description()));
    } else {
      // 如果没有@Tag注解，使用类名作为标签的名称和描述
      tags.add(new Tag()
          .name(controllerClass.getSimpleName())
          .description(controllerClass.getSimpleName() + " API"));
    }

    return tags;
  }

  private PathItem generatePathItem(Class<?> controllerClass, Method method) {
    PathItem pathItem = new PathItem();
    Operation operation = new Operation();

    // 设置操作ID
    operation.setOperationId(method.getName());

    // 设置操作标签
    // 获取类级别的@Tag注解
    io.swagger.v3.oas.annotations.tags.Tag controllerTag = controllerClass.getAnnotation(
        io.swagger.v3.oas.annotations.tags.Tag.class);
    if (controllerTag != null) {
      // 如果有@Tag注解，将注解的名称添加到operation的标签中
      operation.addTagsItem(controllerTag.name());
    } else {
      // 如果没有@Tag注解，将类名添加到operation的标签中
      operation.addTagsItem(controllerClass.getSimpleName());
    }

    // 设置操作类型
    if (method.isAnnotationPresent(GetMapping.class)) {
      pathItem.get(operation);
    } else if (method.isAnnotationPresent(PostMapping.class)) {
      pathItem.post(operation);
    } else if (method.isAnnotationPresent(PutMapping.class)) {
      pathItem.put(operation);
    } else if (method.isAnnotationPresent(PatchMapping.class)) {
      pathItem.patch(operation);
    } else if (method.isAnnotationPresent(DeleteMapping.class)) {
      pathItem.delete(operation);
    }

    // 设置参数
    operation.setParameters(generateParameters(method));

    // 设置请求体
    io.swagger.v3.oas.models.parameters.RequestBody requestBody = generateRequestBody(method);
    if (requestBody != null) {
      operation.setRequestBody(requestBody);
    }

    // 处理方法级别的@Operation注解
    io.swagger.v3.oas.annotations.Operation operationAnnotation = method
        .getAnnotation(io.swagger.v3.oas.annotations.Operation.class);
    if (operationAnnotation != null) {
      operation.setSummary(operationAnnotation.summary());
      operation.setDescription(operationAnnotation.description());
      if (operationAnnotation.deprecated()) {
        operation.setDeprecated(true);
      }

      // 处理方法级别的响应注解
      final io.swagger.v3.oas.annotations.responses.ApiResponse[] operationResponses = operationAnnotation.responses();
      if (operationResponses != null && operationResponses.length > 0) {
        ApiResponses responses = new ApiResponses();
        for (io.swagger.v3.oas.annotations.responses.ApiResponse apiResponse : operationResponses) {
          ApiResponse response = new ApiResponse()
              .description(apiResponse.description());

          // 处理响应内容
          if (apiResponse.content().length > 0) {
            Content content = new Content();
            for (io.swagger.v3.oas.annotations.media.Content contentAnnotation : apiResponse.content()) {
              MediaType mediaType = new MediaType();
              if (contentAnnotation.schema().implementation() != Void.class) {
                Schema<?> schema = createSchema(contentAnnotation.schema().implementation());
                mediaType.setSchema(schema);
              }
              content.addMediaType(contentAnnotation.mediaType(), mediaType);
            }
            response.setContent(content);
          }

          responses.addApiResponse(apiResponse.responseCode(), response);
        }
        operation.setResponses(responses);
      } else {
        // 如果方法级别的响应注解，设置默认响应
        operation.setResponses(generateResponses(method));
      }
    } else {
      // 如果没有@Operation注解，设置默认响应
      operation.setResponses(generateResponses(method));
    }

    return pathItem;
  }

  // 生成参数
  private List<io.swagger.v3.oas.models.parameters.Parameter> generateParameters(Method method) {
    List<io.swagger.v3.oas.models.parameters.Parameter> parameters = new ArrayList<>();

    // 遍历方法参数
    for (Parameter parameter : method.getParameters()) {
      if (parameter.isAnnotationPresent(PathVariable.class)) { // 检查参数是否有 @PathVariable 注解
        // 获取 @PathVariable 注解
        PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
        // 创建参数
        parameters.add(createParameter(
            pathVariable.value().isEmpty() ? parameter.getName() : pathVariable.value(), // 获取参数名称
            "path", // 参数位置
            true, // 是否必需
            parameter.getType(), // 参数类型
            parameter)); // 获取参数
      } else if (parameter.isAnnotationPresent(RequestParam.class)) { // 检查参数是否有 @RequestParam 注解
        // 获取 @RequestParam 注解
        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
        // 创建参数
        parameters.add(createParameter(
            requestParam.value().isEmpty() ? parameter.getName() : requestParam.value(), // 获取参数名称
            "query", // 参数位置
            requestParam.required(), // 是否必需
            parameter.getType(), // 参数类型
            parameter)); // 获取参数
      }
    }

    return parameters;
  }

  // 生成请求体
  private io.swagger.v3.oas.models.parameters.RequestBody generateRequestBody(Method method) {
    // 遍历方法参数
    for (Parameter parameter : method.getParameters()) {
      // 检查参数是否有 @RequestBody 注解
      if (parameter.isAnnotationPresent(RequestBody.class)) {
        Schema<?> schema;
        // 获取参数类型
        Type parameterType = parameter.getParameterizedType();
        // 检查参数类型是否是泛型类型
        if (parameterType instanceof ParameterizedType parameterizedType) {
          // 检查是否是 ApiRequest
          if (ApiRequest.class.equals(getRawType(parameterizedType))) {
            // 获取实际类型参数
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            // 检查实际类型参数的数量
            if (typeArguments.length > 0) {
              // 创建 ApiRequest 的 schema
              Schema<?> apiRequestSchema = createSchemaFromClass(ApiRequest.class);

              // 创建实际类型的 schema
              Schema<?> dataSchema = createSchema(typeArguments[0]);

              // 添加属性到 ApiRequest schema
              apiRequestSchema.addProperties("data", dataSchema);

              // 设置 ApiRequest 的 schema
              schema = apiRequestSchema;
            } else {
              // 如果没有实际类型参数，使用 ApiRequest 的 schema
              schema = createSchemaFromClass(ApiRequest.class);
            }
          } else {
            // 参数类型是其他泛型类型
            schema = createSchema(parameterType);
          }
        } else {
          // 参数类型是非泛型类型
          schema = createSchema(parameterType);
        }

        // 创建请求体
        io.swagger.v3.oas.models.parameters.RequestBody requestBody = new io.swagger.v3.oas.models.parameters.RequestBody()
            .required(true) // 设置请求体为必需
            .content(new Content() // 设置请求体内容
                .addMediaType("application/json", // 设置媒体类型
                    new MediaType().schema(schema))); // 设置请求体的 schema

        // 处理 @Parameter 注解的描述
        io.swagger.v3.oas.annotations.Parameter parameterAnnotation = parameter
            .getAnnotation(io.swagger.v3.oas.annotations.Parameter.class);
        if (parameterAnnotation != null) {
          requestBody.setDescription(parameterAnnotation.description());
        }

        // 返回请求体
        return requestBody;
      }
    }
    // 如果没有找到 @RequestBody 注解，返回 null
    return null;
  }

  private ApiResponses generateResponses(Method method) {
    ApiResponses responses = new ApiResponses();
    Type returnType = method.getGenericReturnType();

    ApiResponse successResponse = new ApiResponse()
        .description(HttpStatus.OK.getReasonPhrase())
        .content(new Content()
            .addMediaType("application/json",
                new MediaType().schema(createSchema(returnType))));

    responses.addApiResponse(String.valueOf(HttpStatus.OK.value()), successResponse);
    return responses;
  }

  private Map<String, Schema> generateSchemas(Method method) {
    Map<String, Schema> schemas = new HashMap<>();

    // 处理返回类型（包括泛型）
    Type returnType = method.getGenericReturnType();
    addSchemas(schemas, returnType);

    // 处理参数类型（包括泛型）
    for (java.lang.reflect.Parameter parameter : method.getParameters()) {
      Type parameterType = parameter.getParameterizedType();
      addSchemas(schemas, parameterType);
    }

    return schemas;
  }

  private io.swagger.v3.oas.models.parameters.Parameter createParameter(
      String name,
      String in,
      boolean required,
      Class<?> type,
      Parameter parameter) {

    io.swagger.v3.oas.models.parameters.Parameter openApiParameter = new io.swagger.v3.oas.models.parameters.Parameter()
        .name(name)
        .in(in)
        .required(required);

    // 使用 OpenAPI 内置工具创建 Schema
    Schema<?> schema = createSchema(type);

    // 处理参数描述
    io.swagger.v3.oas.annotations.Parameter parameterAnnotation = parameter
        .getAnnotation(io.swagger.v3.oas.annotations.Parameter.class);
    if (parameterAnnotation != null) {
      openApiParameter.setDescription(parameterAnnotation.description());
      if (!parameterAnnotation.example().isEmpty()) {
        schema.example(parameterAnnotation.example());
      }
    }

    openApiParameter.setSchema(schema);
    return openApiParameter;
  }

  private Schema<?> createSchema(Type type) {
    // 处理原始类型
    if (type instanceof Class<?> clazz) {
      return createSchemaFromClass(clazz);
    }

    // 处理泛型类型
    if (type instanceof ParameterizedType parameterizedType) {
      return createSchemaFromParameterizedType(parameterizedType);
    }

    return new Schema<>();
  }

  private Schema<?> createSchemaFromClass(Class<?> clazz) {
    // 处理基本类型
    PrimitiveType primitiveType = PrimitiveType.fromType(clazz);
    if (primitiveType != null) {
      return primitiveType.createProperty();
    }

    // 处理复杂类型
    ResolvedSchema resolvedSchema = ModelConverters.getInstance()
        .resolveAsResolvedSchema(new AnnotatedType(clazz));
    if (resolvedSchema != null && resolvedSchema.schema != null) {
      return resolvedSchema.schema;
    }

    // 默认返回引用类型
    return new Schema<>().$ref("#/components/schemas/" + clazz.getSimpleName());
  }

  private Schema<?> createSchemaFromParameterizedType(ParameterizedType parameterizedType) {
    Type rawType = parameterizedType.getRawType();
    Type[] typeArguments = parameterizedType.getActualTypeArguments();

    // 处理集合类型
    if (rawType instanceof Class<?> && Collection.class.isAssignableFrom((Class<?>) rawType)) {
      return createArraySchema(typeArguments[0]);
    }

    // 处理Map类型
    if (rawType instanceof Class<?> && Map.class.isAssignableFrom((Class<?>) rawType)) {
      return createMapSchema(typeArguments[1]);
    }

    // 处理其他泛型类型（如Page<T>）
    return createGenericSchema(parameterizedType);
  }

  private Schema<?> createArraySchema(Type elementType) {
    ArraySchema arraySchema = new ArraySchema();
    arraySchema.setItems(createSchema(elementType));
    return arraySchema;
  }

  private Schema<?> createMapSchema(Type valueType) {
    Schema<?> schema = new Schema<>();
    schema.setType("object");
    schema.setAdditionalProperties(createSchema(valueType));
    return schema;
  }

  private Schema<?> createGenericSchema(ParameterizedType parameterizedType) {
    Class<?> rawClass = (Class<?>) parameterizedType.getRawType();
    Type[] typeArguments = parameterizedType.getActualTypeArguments();

    // 创建泛型类型的schema
    String schemaName = generateGenericSchemaName(rawClass, typeArguments);
    return new Schema<>().$ref("#/components/schemas/" + schemaName);
  }

  private String generateGenericSchemaName(Class<?> rawClass, Type[] typeArguments) {
    StringBuilder name = new StringBuilder(rawClass.getSimpleName());

    if (typeArguments != null && typeArguments.length > 0) {
      name.append("_");
      for (Type arg : typeArguments) {
        if (arg instanceof Class) {
          name.append(((Class<?>) arg).getSimpleName());
        } else if (arg instanceof ParameterizedType) {
          name.append(generateGenericSchemaName(
              (Class<?>) ((ParameterizedType) arg).getRawType(),
              ((ParameterizedType) arg).getActualTypeArguments()));
        }
      }
    }

    return name.toString();
  }

  private void addSchemas(Map<String, Schema> schemas, Type type) {
    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      Class<?> rawClass = (Class<?>) parameterizedType.getRawType();
      Type[] typeArguments = parameterizedType.getActualTypeArguments();

      // 添加泛型类型的schema
      String schemaName = generateGenericSchemaName(rawClass, typeArguments);
      if (!schemas.containsKey(schemaName)) {
        Schema<?> schema = createGenericTypeSchema(parameterizedType);
        schemas.put(schemaName, schema);
      }

      // 添加泛型参数的schemas
      for (Type typeArg : typeArguments) {
        addSchemas(schemas, typeArg);
      }

      // 添加原始类型的schema
      addTypeSchema(schemas, rawClass);
    } else if (type instanceof Class<?>) {
      addTypeSchema(schemas, type);
    }
  }

  private void addTypeSchema(Map<String, Schema> schemas, Type type) {
    if (type instanceof Class<?>) {
      Class<?> clazz = (Class<?>) type;
      if (!clazz.isPrimitive() && !clazz.getName().startsWith("java.lang")) {
        ResolvedSchema resolvedSchema = ModelConverters.getInstance()
            .resolveAsResolvedSchema(new AnnotatedType(type));
        if (resolvedSchema != null) {
          if (resolvedSchema.schema != null) {
            schemas.putIfAbsent(clazz.getSimpleName(), resolvedSchema.schema);
          }
          if (resolvedSchema.referencedSchemas != null) {
            resolvedSchema.referencedSchemas.forEach(schemas::putIfAbsent);
          }
        }
      }
    }
  }

  private Schema<?> createGenericTypeSchema(ParameterizedType parameterizedType) {
    Class<?> rawClass = (Class<?>) parameterizedType.getRawType();
    Type[] typeArguments = parameterizedType.getActualTypeArguments();

    // 获取原始类型的schema
    ResolvedSchema resolvedSchema = ModelConverters.getInstance()
        .resolveAsResolvedSchema(new AnnotatedType(rawClass));

    if (resolvedSchema != null && resolvedSchema.schema != null) {
      Schema<?> schema = resolvedSchema.schema;

      // 处理泛型属性
      Map<String, Schema> properties = schema.getProperties();
      if (properties != null) {
        for (Map.Entry<String, Schema> entry : properties.entrySet()) {
          // 如果属性是泛型类型，使用实际类型替换
          if (entry.getValue() instanceof ArraySchema) {
            ArraySchema arraySchema = (ArraySchema) entry.getValue();
            arraySchema.setItems(createSchema(typeArguments[0]));
          } else if ("object".equals(entry.getValue().getType())) {
            entry.setValue(createSchema(typeArguments[0]));
          }
        }
      }

      return schema;
    }

    // 默认返回引用类型
    String schemaName = generateGenericSchemaName(rawClass, typeArguments);
    return new Schema<>().$ref("#/components/schemas/" + schemaName);
  }

  // 辅助方法：获取原始类型
  private Class<?> getRawType(Type type) {
    if (type instanceof Class<?>) {
      return (Class<?>) type;
    }
    if (type instanceof ParameterizedType) {
      return (Class<?>) ((ParameterizedType) type).getRawType();
    }
    throw new IllegalArgumentException("Unexpected type: " + type);
  }

  // 查找指定方法
  private Method findTargetMethod(Class<?> controllerClass, String methodName) {
    // 查找指定方法
    for (Method method : controllerClass.getDeclaredMethods()) {
      // 检查方法名是否匹配
      if (method.getName().equals(methodName)) {
        // 如果方法名匹配，返回该方法
        return method;
      }
    }
    // 如果没有找到指定方法，抛出异常
    throw new IllegalArgumentException("Method " + methodName + " not found in " + controllerClass.getName());
  }

  // 辅助方法：获取完整路径
  private String getFullPath(Class<?> controllerClass, Method method) {
    String classPath = "";
    String methodPath = "";

    // 获取类级别的@RequestMapping注解
    RequestMapping classMapping = AnnotationUtils.findAnnotation(controllerClass, RequestMapping.class);
    if (classMapping != null && classMapping.value().length > 0) {
      // 获取类级别的路径
      classPath = classMapping.value()[0];
    }

    // 获取方法级别的@RequestMapping注解
    if (method.isAnnotationPresent(GetMapping.class)) {
      GetMapping mapping = method.getAnnotation(GetMapping.class);
      methodPath = mapping.value().length > 0 ? mapping.value()[0] : "";
    } else if (method.isAnnotationPresent(PostMapping.class)) {
      PostMapping mapping = method.getAnnotation(PostMapping.class);
      methodPath = mapping.value().length > 0 ? mapping.value()[0] : "";
    } else if (method.isAnnotationPresent(PutMapping.class)) {
      PutMapping mapping = method.getAnnotation(PutMapping.class);
      methodPath = mapping.value().length > 0 ? mapping.value()[0] : "";
    } else if (method.isAnnotationPresent(DeleteMapping.class)) {
      DeleteMapping mapping = method.getAnnotation(DeleteMapping.class);
      methodPath = mapping.value().length > 0 ? mapping.value()[0] : "";
    }

    // 确保路径以/开头
    if (!classPath.startsWith("/")) {
      classPath = "/" + classPath;
    }
    if (!methodPath.startsWith("/")) {
      methodPath = "/" + methodPath;
    }

    // 拼接路径
    return (classPath + methodPath).replaceAll("//", "/");
  }

  // 输出到控制台
  public void printOpenAPI(OpenAPI openAPI) {
    try {
      String jsonOutput = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(openAPI);
      System.out.println(jsonOutput);
    } catch (Exception e) {
      log.error("Error occurs in printOpenAPI", e);
    }
  }

  // 保存到文件
  public void saveToFile(OpenAPI openAPI, String filePath) {
    try {
      objectMapper.writerWithDefaultPrettyPrinter()
          .writeValue(new File(filePath), openAPI);
    } catch (Exception e) {
      log.error("Error occurs in saveToFile", e);
    }
  }
}
