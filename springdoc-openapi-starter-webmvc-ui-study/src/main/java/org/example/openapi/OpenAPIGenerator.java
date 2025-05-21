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
    PathItem pathItem = generatePathItem(targetMethod, basePath);
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

  private Method findTargetMethod(Class<?> controllerClass, String methodName) {
    for (Method method : controllerClass.getDeclaredMethods()) {
      if (method.getName().equals(methodName)) {
        return method;
      }
    }
    return null;
  }

  private PathItem generatePathItem(Method method, String basePath) {
    PathItem pathItem = new PathItem();
    Operation operation = new Operation();

    // 处理方法级别的路径
    String methodPath = "";
    if (method.isAnnotationPresent(GetMapping.class)) {
      methodPath = method.getAnnotation(GetMapping.class).value()[0];
      pathItem.get(operation);
    } else if (method.isAnnotationPresent(PostMapping.class)) {
      methodPath = method.getAnnotation(PostMapping.class).value()[0];
      pathItem.post(operation);
    } else if (method.isAnnotationPresent(PutMapping.class)) {
      methodPath = method.getAnnotation(PutMapping.class).value()[0];
      pathItem.put(operation);
    } else if (method.isAnnotationPresent(DeleteMapping.class)) {
      methodPath = method.getAnnotation(DeleteMapping.class).value()[0];
      pathItem.delete(operation);
    }

    // 设置完整路径
    pathItem.set$ref(basePath + methodPath);

    // 处理操作详情
    operation.setOperationId(method.getName());
    operation.setParameters(generateParameters(method));
    operation.setRequestBody(generateRequestBody(method));
    operation.setResponses(generateResponses(method));

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
        return new io.swagger.v3.oas.models.parameters.RequestBody()
            .required(true)
            .content(new Content()
                .addMediaType("application/json",
                    new MediaType().schema(new Schema().$ref(parameter.getType().getSimpleName()))));
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
