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
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
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

  private Schema<?> createSchema(Class<?> type) {
    // 使用 PrimitiveType 处理基本类型
    PrimitiveType primitiveType = PrimitiveType.fromType(type);
    if (primitiveType != null) {
      return primitiveType.createProperty();
    }

    // 使用 ModelConverters 处理复杂类型
    ResolvedSchema resolvedSchema = ModelConverters.getInstance()
        .resolveAsResolvedSchema(new AnnotatedType(type));

    if (resolvedSchema != null && resolvedSchema.schema != null) {
      return resolvedSchema.schema;
    }

    // fallback 处理
    return new Schema<>().$ref("#/components/schemas/" + type.getSimpleName());
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

  private Map<String, Schema> generateSchemas(Method method) {
    ModelConverters converters = ModelConverters.getInstance();
    Map<String, Schema> schemas = converters.read(method.getReturnType());

    // 处理返回类型
    ResolvedSchema resolvedSchema = ModelConverters.getInstance()
        .resolveAsResolvedSchema(new AnnotatedType(method.getReturnType()));
    if (resolvedSchema != null && resolvedSchema.referencedSchemas != null) {
      schemas.putAll(resolvedSchema.referencedSchemas);
    }

    // 处理参数类型
    for (java.lang.reflect.Parameter parameter : method.getParameters()) {
      resolvedSchema = ModelConverters.getInstance()
          .resolveAsResolvedSchema(new AnnotatedType(parameter.getType()));
      if (resolvedSchema != null && resolvedSchema.referencedSchemas != null) {
        schemas.putAll(resolvedSchema.referencedSchemas);
      }
    }

    // 添加请求体的Schema
    for (Parameter parameter : method.getParameters()) {
      if (parameter.isAnnotationPresent(RequestBody.class)) {
        schemas.putAll(converters.read(parameter.getType()));
      }
    }

    return schemas;
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
