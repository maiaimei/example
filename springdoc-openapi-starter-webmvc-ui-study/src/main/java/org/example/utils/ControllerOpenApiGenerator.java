package org.example.utils;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponses;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.model.ErrorResponse;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
public class ControllerOpenApiGenerator {

  private final SchemaGenerator schemaGenerator;

  public ControllerOpenApiGenerator() {
    this.schemaGenerator = new SchemaGenerator();
  }

  public Paths generatePaths(Class<?> controllerClass) {
    Paths paths = new Paths();

    try {
      // 获取基础路径
      String basePath = getBasePath(controllerClass);

      // 获取控制器名称（用于分组）
      String controllerName = getControllerName(controllerClass);

      // 处理所有公共方法
      for (Method method : controllerClass.getDeclaredMethods()) {
        if (Modifier.isPublic(method.getModifiers())) {
          processMethod(method, basePath, paths, controllerName);
        }
      }
    } catch (Exception e) {
      log.error("Failed to generate OpenAPI paths for controller: " + controllerClass.getName(), e);
    }

    return paths;
  }

  private String getBasePath(Class<?> controllerClass) {
    // 处理类级别的路径映射
    String basePath = "";
    RequestMapping classMapping = AnnotationUtils.findAnnotation(controllerClass, RequestMapping.class);
    if (classMapping != null && classMapping.value().length > 0) {
      basePath = classMapping.value()[0];
    }
    return basePath;
  }

  private String getControllerName(Class<?> controllerClass) {
    // 从类名中提取控制器名称
    String className = controllerClass.getSimpleName();
    if (className.endsWith("Controller")) {
      return className.substring(0, className.length() - "Controller".length());
    }
    return className;
  }

  private void processMethod(Method method, String basePath, Paths paths, String controllerName) {
    // 获取方法的路径和HTTP方法
    RequestMappingInfo mappingInfo = getRequestMappingInfo(method);
    if (mappingInfo == null) {
      return;
    }

    String path = buildPath(basePath, mappingInfo.getPath());
    PathItem pathItem = paths.get(path);
    if (pathItem == null) {
      pathItem = new PathItem();
      paths.addPathItem(path, pathItem);
    }

    // 创建Operation
    Operation operation = createOperation(method, controllerName);

    // 设置Operation到PathItem
    setOperationForHttpMethod(pathItem, mappingInfo.getHttpMethod(), operation);
  }

  private RequestMappingInfo getRequestMappingInfo(Method method) {
    // 处理各种请求映射注解
    if (method.isAnnotationPresent(GetMapping.class)) {
      GetMapping annotation = method.getAnnotation(GetMapping.class);
      return new RequestMappingInfo(getFirstValue(annotation.value()), RequestMethod.GET);
    }
    if (method.isAnnotationPresent(PostMapping.class)) {
      PostMapping annotation = method.getAnnotation(PostMapping.class);
      return new RequestMappingInfo(getFirstValue(annotation.value()), RequestMethod.POST);
    }
    if (method.isAnnotationPresent(PutMapping.class)) {
      PutMapping annotation = method.getAnnotation(PutMapping.class);
      return new RequestMappingInfo(getFirstValue(annotation.value()), RequestMethod.PUT);
    }
    if (method.isAnnotationPresent(DeleteMapping.class)) {
      DeleteMapping annotation = method.getAnnotation(DeleteMapping.class);
      return new RequestMappingInfo(getFirstValue(annotation.value()), RequestMethod.DELETE);
    }
    if (method.isAnnotationPresent(PatchMapping.class)) {
      PatchMapping annotation = method.getAnnotation(PatchMapping.class);
      return new RequestMappingInfo(getFirstValue(annotation.value()), RequestMethod.PATCH);
    }
    if (method.isAnnotationPresent(RequestMapping.class)) {
      RequestMapping annotation = method.getAnnotation(RequestMapping.class);
      RequestMethod[] methods = annotation.method();
      return new RequestMappingInfo(
          getFirstValue(annotation.value()),
          methods.length > 0 ? methods[0] : RequestMethod.GET
      );
    }
    return null;
  }

  private String getFirstValue(String[] values) {
    return values.length > 0 ? values[0] : "";
  }

  private String buildPath(String basePath, String methodPath) {
    StringBuilder path = new StringBuilder();
    if (StringUtils.hasText(basePath)) {
      path.append("/").append(basePath);
    }
    if (StringUtils.hasText(methodPath)) {
      path.append("/").append(methodPath);
    }
    return path.toString().replaceAll("//+", "/");
  }

  private Operation createOperation(Method method, String controllerName) {
    Operation operation = new Operation();

    // 设置操作ID（控制器名称 + 方法名）
    operation.operationId(controllerName + "_" + method.getName());

    // 设置描述（从方法名生成）
    setDescription(method, operation);

    // 设置标签（使用控制器名称）
    operation.addTagsItem(controllerName);

    // 处理参数
    processParameters(method, operation);

    // 处理请求体
    processRequestBody(method, operation);

    // 处理响应
    processResponses(method, operation);

    return operation;
  }

  private void setDescription(Method method, Operation operation) {
    // 从方法名生成描述
    String methodName = method.getName();
    String description = StringUtils.capitalize(
        String.join(" ", methodName.split("(?=\\p{Upper})"))
    );
    operation.summary(description);
  }

  private void processParameters(Method method, Operation operation) {
    Parameter[] parameters = method.getParameters();
    for (Parameter parameter : parameters) {
      // 跳过请求体参数
      if (parameter.isAnnotationPresent(RequestBody.class)) {
        continue;
      }

      // 处理路径参数
      PathVariable pathVar = parameter.getAnnotation(PathVariable.class);
      if (pathVar != null) {
        operation.addParametersItem(createPathParameter(parameter, pathVar));
        continue;
      }

      // 处理查询参数
      RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
      if (requestParam != null) {
        operation.addParametersItem(createQueryParameter(parameter, requestParam));
        continue;
      }

      // 处理请求头
      RequestHeader requestHeader = parameter.getAnnotation(RequestHeader.class);
      if (requestHeader != null) {
        operation.addParametersItem(createHeaderParameter(parameter, requestHeader));
      }
    }
  }

  private io.swagger.v3.oas.models.parameters.Parameter createPathParameter(
      Parameter parameter, PathVariable pathVar) {
    String name = StringUtils.hasText(pathVar.value()) ? pathVar.value() : parameter.getName();
    return new io.swagger.v3.oas.models.parameters.Parameter()
        .name(name)
        .in("path")
        .required(true)
        .description("Path parameter: " + name)
        .schema(schemaGenerator.generateSchema(parameter.getType()));
  }

  private io.swagger.v3.oas.models.parameters.Parameter createQueryParameter(
      Parameter parameter, RequestParam requestParam) {
    String name = StringUtils.hasText(requestParam.value()) ? requestParam.value() : parameter.getName();
    return new io.swagger.v3.oas.models.parameters.Parameter()
        .name(name)
        .in("query")
        .required(requestParam.required())
        .description("Query parameter: " + name)
        .schema(schemaGenerator.generateSchema(parameter.getType()));
  }

  private io.swagger.v3.oas.models.parameters.Parameter createHeaderParameter(
      Parameter parameter, RequestHeader requestHeader) {
    String name = StringUtils.hasText(requestHeader.value()) ? requestHeader.value() : parameter.getName();
    return new io.swagger.v3.oas.models.parameters.Parameter()
        .name(name)
        .in("header")
        .required(requestHeader.required())
        .description("Header parameter: " + name)
        .schema(schemaGenerator.generateSchema(parameter.getType()));
  }

  private void processRequestBody(Method method, Operation operation) {
    Parameter[] parameters = method.getParameters();
    for (Parameter parameter : parameters) {
      RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
      if (requestBody != null) {
        operation.requestBody(createRequestBody(parameter));
        break; // 只处理第一个请求体
      }
    }
  }

  private io.swagger.v3.oas.models.parameters.RequestBody createRequestBody(Parameter parameter) {
    Type parameterType = parameter.getParameterizedType();
    return new io.swagger.v3.oas.models.parameters.RequestBody()
        .required(true)
        .description("Request body")
        .content(new Content()
            .addMediaType("application/json",
                new MediaType().schema(schemaGenerator.generateSchema(parameterType.getClass()))));
  }

  private void processResponses(Method method, Operation operation) {
    ApiResponses responses = new ApiResponses();

    // 处理返回类型
    Type returnType = method.getGenericReturnType();
    if (returnType != void.class) {
      responses.addApiResponse("200", createSuccessResponse(returnType));
    }

    // 添加通用错误响应
    responses.addApiResponse("400", createErrorResponse("Bad Request"));
    responses.addApiResponse("401", createErrorResponse("Unauthorized"));
    responses.addApiResponse("403", createErrorResponse("Forbidden"));
    responses.addApiResponse("404", createErrorResponse("Not Found"));
    responses.addApiResponse("500", createErrorResponse("Internal Server Error"));

    operation.responses(responses);
  }

  private io.swagger.v3.oas.models.responses.ApiResponse createSuccessResponse(Type returnType) {
    // 处理ResponseEntity
    if (returnType instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) returnType;
      if (ResponseEntity.class.equals(parameterizedType.getRawType())) {
        returnType = parameterizedType.getActualTypeArguments()[0];
      }
    }

    return new io.swagger.v3.oas.models.responses.ApiResponse()
        .description("Successful operation")
        .content(new Content()
            .addMediaType("application/json",
                new MediaType().schema(schemaGenerator.generateSchema(returnType.getClass()))));
  }

  private io.swagger.v3.oas.models.responses.ApiResponse createErrorResponse(String description) {
    return new io.swagger.v3.oas.models.responses.ApiResponse()
        .description(description)
        .content(new Content()
            .addMediaType("application/json",
                new MediaType().schema(schemaGenerator.generateSchema(ErrorResponse.class))));
  }

  private void setOperationForHttpMethod(PathItem pathItem, RequestMethod method, Operation operation) {
    switch (method) {
      case GET:
        pathItem.get(operation);
        break;
      case POST:
        pathItem.post(operation);
        break;
      case PUT:
        pathItem.put(operation);
        break;
      case DELETE:
        pathItem.delete(operation);
        break;
      case PATCH:
        pathItem.patch(operation);
        break;
    }
  }

  @Data
  private static class RequestMappingInfo {

    private final String path;
    private final RequestMethod httpMethod;
  }
}
