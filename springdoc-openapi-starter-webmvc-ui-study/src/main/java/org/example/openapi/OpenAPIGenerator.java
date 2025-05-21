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
import org.springframework.core.annotation.AnnotationUtils;
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

  public OpenAPI generateAPI(Class<?> controllerClass, String methodName) {
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

    // 添加控制器标签
    addControllerTags(openAPI, controllerClass);

    // 获取基础路径
    RequestMapping classMapping = AnnotationUtils.findAnnotation(controllerClass, RequestMapping.class);
    String basePath = "";
    if (classMapping != null && classMapping.value().length > 0) {
      basePath = classMapping.value()[0];
    }

    // 查找指定方法
    Method targetMethod = findTargetMethod(controllerClass, methodName);
    if (targetMethod == null) {
      throw new IllegalArgumentException("Method " + methodName + " not found in " + controllerClass.getName());
    }

    // 生成路径
    Paths paths = new Paths();
    PathItem pathItem = generatePathItem(controllerClass, targetMethod);
    if (pathItem != null && pathItem.readOperations().size() > 0) {
      String fullPath = getFullPath(basePath, targetMethod);
      paths.addPathItem(fullPath, pathItem);
    }
    openAPI.setPaths(paths);

    // 生成组件
    Components components = new Components();
    components.setSchemas(generateSchemas(targetMethod));
    openAPI.setComponents(components);

    return openAPI;
  }

  private void addControllerTags(OpenAPI openAPI, Class<?> controllerClass) {
    List<Tag> tags = new ArrayList<>();

    // 处理控制器级别的@Tag注解
    io.swagger.v3.oas.annotations.tags.Tag controllerTag = controllerClass.getAnnotation(
        io.swagger.v3.oas.annotations.tags.Tag.class);
    if (controllerTag != null) {
      tags.add(new Tag()
          .name(controllerTag.name())
          .description(controllerTag.description()));
    } else {
      // 如果没有@Tag注解，使用类名作为标签
      tags.add(new Tag()
          .name(controllerClass.getSimpleName())
          .description(controllerClass.getSimpleName() + " API"));
    }

    openAPI.setTags(tags);
  }

  private Method findTargetMethod(Class<?> controllerClass, String methodName) {
    for (Method method : controllerClass.getDeclaredMethods()) {
      if (method.getName().equals(methodName)) {
        return method;
      }
    }
    return null;
  }

  private PathItem generatePathItem(Class<?> controllerClass, Method method) {
    PathItem pathItem = new PathItem();
    Operation operation = new Operation();

    // 设置operationId
    operation.setOperationId(method.getName());

    // 设置操作的标签
    io.swagger.v3.oas.annotations.tags.Tag controllerTag = controllerClass.getAnnotation(
        io.swagger.v3.oas.annotations.tags.Tag.class);
    if (controllerTag != null) {
      operation.addTagsItem(controllerTag.name());
    } else {
      operation.addTagsItem(controllerClass.getSimpleName());
    }

    // 根据HTTP方法类型设置operation
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
    io.swagger.v3.oas.annotations.Operation operationAnnotation =
        method.getAnnotation(io.swagger.v3.oas.annotations.Operation.class);
    if (operationAnnotation != null) {
      operation.setSummary(operationAnnotation.summary());
      operation.setDescription(operationAnnotation.description());
      if (operationAnnotation.deprecated()) {
        operation.setDeprecated(true);
      }

      // 处理方法级别的响应注解
      ApiResponses responses = new ApiResponses();
      for (io.swagger.v3.oas.annotations.responses.ApiResponse apiResponse : operationAnnotation.responses()) {
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
      // 如果没有@Operation注解，设置默认响应
      operation.setResponses(generateResponses(method));
    }

    return pathItem;
  }

  private String getFullPath(String basePath, Method method) {
    String methodPath = "";

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
    if (!basePath.startsWith("/")) {
      basePath = "/" + basePath;
    }
    if (!methodPath.startsWith("/")) {
      methodPath = "/" + methodPath;
    }

    return (basePath + methodPath).replaceAll("//", "/");
  }

  private List<io.swagger.v3.oas.models.parameters.Parameter> generateParameters(Method method) {
    List<io.swagger.v3.oas.models.parameters.Parameter> parameters = new ArrayList<>();

    for (Parameter parameter : method.getParameters()) {
      if (parameter.isAnnotationPresent(PathVariable.class)) {
        PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
        parameters.add(createParameter(
            pathVariable.value().isEmpty() ? parameter.getName() : pathVariable.value(),
            "path",
            true,
            parameter.getType(),
            parameter
        ));
      } else if (parameter.isAnnotationPresent(RequestParam.class)) {
        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
        parameters.add(createParameter(
            requestParam.value().isEmpty() ? parameter.getName() : requestParam.value(),
            "query",
            requestParam.required(),
            parameter.getType(),
            parameter
        ));
      }
    }

    return parameters;
  }

  private io.swagger.v3.oas.models.parameters.Parameter createParameter(
      String name,
      String in,
      boolean required,
      Class<?> type,
      Parameter parameter) {

    io.swagger.v3.oas.models.parameters.Parameter openApiParameter =
        new io.swagger.v3.oas.models.parameters.Parameter()
            .name(name)
            .in(in)
            .required(required);

    // 使用 OpenAPI 内置工具创建 Schema
    Schema<?> schema = createSchema(type);

    // 处理参数描述
    io.swagger.v3.oas.annotations.Parameter parameterAnnotation =
        parameter.getAnnotation(io.swagger.v3.oas.annotations.Parameter.class);
    if (parameterAnnotation != null) {
      openApiParameter.setDescription(parameterAnnotation.description());
      if (!parameterAnnotation.example().isEmpty()) {
        schema.example(parameterAnnotation.example());
      }
    }

    openApiParameter.setSchema(schema);
    return openApiParameter;
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

  private Schema<?> createSchema(Type type) {
    // 处理原始类型
    if (type instanceof Class<?>) {
      Class<?> clazz = (Class<?>) type;
      return createSchemaFromClass(clazz);
    }

    // 处理泛型类型
    if (type instanceof ParameterizedType) {
      return createSchemaFromParameterizedType((ParameterizedType) type);
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
              ((ParameterizedType) arg).getActualTypeArguments()
          ));
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

  private io.swagger.v3.oas.models.parameters.RequestBody generateRequestBody(Method method) {
    for (Parameter parameter : method.getParameters()) {
      if (parameter.isAnnotationPresent(RequestBody.class)) {
        io.swagger.v3.oas.models.parameters.RequestBody requestBody = new io.swagger.v3.oas.models.parameters.RequestBody()
            .required(true)
            .content(new Content()
                .addMediaType("application/json",
                    new MediaType().schema(createSchema(parameter.getType()))));
        // 处理@RequestBody注解的描述
        io.swagger.v3.oas.annotations.Parameter parameterAnnotation =
            parameter.getAnnotation(io.swagger.v3.oas.annotations.Parameter.class);
        if (parameterAnnotation != null) {
          requestBody.setDescription(parameterAnnotation.description());
        }
        return requestBody;
      }
    }
    return null;
  }

  private ApiResponses generateResponses(Method method) {
    ApiResponses responses = new ApiResponses();
    Class<?> returnType = method.getReturnType();

    ApiResponse response = new ApiResponse()
        .description("Successful Operation")
        .content(new Content()
            .addMediaType("application/json",
                new MediaType().schema(createSchema(method.getReturnType()))));

    responses.addApiResponse("200", response);
    return responses;
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
