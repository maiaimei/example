package org.example.openapi;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;

/**
 * OpenAPI定制器 - 处理文件和二进制响应
 */
public class OpenAPIFileRelatedCustomizer {

  private final Operation operation;
  private final HandlerMethod handlerMethod;
  private final Type returnType;

  public OpenAPIFileRelatedCustomizer(Operation operation, HandlerMethod handlerMethod, Type returnType) {
    this.operation = operation;
    this.handlerMethod = handlerMethod;
    this.returnType = returnType;
  }

  public void customise() {
    // 获取响应的Content-Type
    String contentType = getResponseContentType(this.handlerMethod);

    if (isStreamType((Class<?>) returnType)) {
      handleStreamResponse(operation);
    } else if (contentType.contains("multipart")) {
      handleMultipartResponse(operation);
    } else {
      // 处理特定文件类型或默认二进制响应
      handleBinaryResponse(operation, contentType);
    }

    // 处理大文件下载场景
    if (isLargeFileDownload(this.handlerMethod)) {
      handleLargeFileDownload(operation);
    }
  }

  /**
   * 判断是否需要跳过处理（文件或二进制响应）
   */
  public boolean shouldSkipCustomise(HandlerMethod handlerMethod, Type returnType) {
    // 检查返回类型
    if (returnType instanceof Class<?> returnClass) {

      // 检查各种二进制相关类型
      return isFileType(returnClass) ||
          isBinaryType(returnClass) ||
          isStreamType(returnClass) ||
          hasFileRelatedAnnotations(handlerMethod);
    }
    return false;
  }

  /**
   * 检查是否为文件类型
   */
  private boolean isFileType(Class<?> clazz) {
    return File.class.isAssignableFrom(clazz) ||
        MultipartFile.class.isAssignableFrom(clazz) ||
        Resource.class.isAssignableFrom(clazz) ||
        Path.class.isAssignableFrom(clazz);
  }

  /**
   * 检查是否为二进制类型
   */
  private boolean isBinaryType(Class<?> clazz) {
    return byte[].class.equals(clazz) ||
        Byte[].class.equals(clazz) ||
        ByteBuffer.class.isAssignableFrom(clazz);
  }

  /**
   * 检查是否为流类型
   */
  private boolean isStreamType(Class<?> clazz) {
    return InputStream.class.isAssignableFrom(clazz) ||
        OutputStream.class.isAssignableFrom(clazz) ||
        ResponseEntity.class.isAssignableFrom(clazz);
  }

  /**
   * 检查是否包含文件相关注解
   */
  private boolean hasFileRelatedAnnotations(HandlerMethod handlerMethod) {
    // 获取方法对象
    Method method = handlerMethod.getMethod();

    // 检查是否有 @ResponseBody 注解
    boolean hasResponseBody = method.isAnnotationPresent(ResponseBody.class) ||
        handlerMethod.getBeanType().isAnnotationPresent(RestController.class);

    // 检查是否有相关的 Mapping 注解
    boolean hasMappingAnnotation = false;
    String contentType = null;

    // 获取所有注解并检查
    for (Annotation annotation : method.getAnnotations()) {
      if (annotation instanceof GetMapping) {
        hasMappingAnnotation = true;
        contentType = String.join(", ", ((GetMapping) annotation).produces());
      } else if (annotation instanceof PostMapping) {
        hasMappingAnnotation = true;
        contentType = String.join(", ", ((PostMapping) annotation).produces());
      } else if (annotation instanceof PutMapping) {
        hasMappingAnnotation = true;
        contentType = String.join(", ", ((PutMapping) annotation).produces());
      } else if (annotation instanceof RequestMapping) {
        hasMappingAnnotation = true;
        contentType = String.join(", ", ((RequestMapping) annotation).produces());
      }
    }

    // 检查 Content-Type
    boolean hasFileContentType = contentType != null && (
        contentType.startsWith("multipart/") ||
            contentType.startsWith("application/octet-stream") ||
            contentType.startsWith("application/pdf") ||
            contentType.contains("download")
    );

    return hasResponseBody && hasMappingAnnotation && hasFileContentType;
  }

  /**
   * 从注解中提取Content-Type
   */
  private String extractContentType(Method method) {
    // 检查各种 Mapping 注解
    if (method.isAnnotationPresent(GetMapping.class)) {
      return String.join(", ", method.getAnnotation(GetMapping.class).produces());
    }
    if (method.isAnnotationPresent(PostMapping.class)) {
      return String.join(", ", method.getAnnotation(PostMapping.class).produces());
    }
    if (method.isAnnotationPresent(PutMapping.class)) {
      return String.join(", ", method.getAnnotation(PutMapping.class).produces());
    }
    if (method.isAnnotationPresent(RequestMapping.class)) {
      return String.join(", ", method.getAnnotation(RequestMapping.class).produces());
    }
    return null;
  }

  /**
   * 获取响应的Content-Type
   */
  private String getResponseContentType(HandlerMethod handlerMethod) {
    Method method = handlerMethod.getMethod();
    String contentType = extractContentType(method);
    return contentType != null ? contentType : "application/octet-stream";
  }

  /**
   * 检查是否为大文件下载
   */
  private boolean isLargeFileDownload(HandlerMethod handlerMethod) {
    Method method = handlerMethod.getMethod();

    // 检查自定义注解
    boolean hasLargeFileAnnotation = method.isAnnotationPresent(LargeFile.class);

    // 检查 Content-Type
    String contentType = extractContentType(method);
    boolean hasDownloadContentType = contentType != null && contentType.contains("download");

    return hasLargeFileAnnotation || hasDownloadContentType;
  }

  /**
   * 处理二进制响应
   */
  private void handleBinaryResponse(Operation operation, String contentType) {
    Schema<?> binarySchema = new Schema<>()
        .type("string")
        .format("binary");

    MediaType mediaType = new MediaType().schema(binarySchema);

    // 根据不同的内容类型添加特定的描述和示例
    if (contentType.contains("pdf")) {
      binarySchema.description("PDF Document");
      mediaType.example("PDF File Content");
    } else if (contentType.contains("image")) {
      binarySchema.description("Image File");
      mediaType.example("Image File Content");
    } else {
      binarySchema.description("Binary File");
      mediaType.example("Binary Content");
    }

    ApiResponse apiResponse = new ApiResponse()
        .description("File Download")
        .content(new Content().addMediaType(contentType, mediaType));

    operation.getResponses().addApiResponse("200", apiResponse);
  }

  /**
   * 处理流式响应
   */
  private void handleStreamResponse(Operation operation) {
    Schema<?> streamSchema = new Schema<>()
        .type("string")
        .format("binary")
        .description("Stream of data");

    ApiResponse apiResponse = new ApiResponse()
        .description("Stream Response")
        .content(new Content()
            .addMediaType("application/octet-stream",
                new MediaType().schema(streamSchema)));

    operation.getResponses().addApiResponse("200", apiResponse);
  }

  /**
   * 处理Multipart响应
   */
  private void handleMultipartResponse(Operation operation) {
    Schema<?> multipartSchema = new Schema<>()
        .type("object")
        .format("binary")
        .description("Multipart File");

    ApiResponse apiResponse = new ApiResponse()
        .description("Multipart Response")
        .content(new Content()
            .addMediaType("multipart/form-data",
                new MediaType().schema(multipartSchema)));

    operation.getResponses().addApiResponse("200", apiResponse);
  }

  /**
   * 处理大文件下载
   */
  private void handleLargeFileDownload(Operation operation) {
    // 添加文件大小响应头
    operation.addParametersItem(new Parameter()
        .in("header")
        .name("Content-Length")
        .description("File size in bytes")
        .schema(new Schema<>().type("integer").format("int64")));

    // 添加范围请求支持
    operation.addParametersItem(new Parameter()
        .in("header")
        .name("Range")
        .description("Bytes range request")
        .schema(new Schema<>().type("string")));

    // 添加断点续传支持
    ApiResponse partialContent = new ApiResponse()
        .description("Partial Content")
        .content(new Content()
            .addMediaType("application/octet-stream",
                new MediaType().schema(new Schema<>()
                    .type("string")
                    .format("binary"))));

    operation.getResponses().addApiResponse("206", partialContent);
  }

  /**
   * 创建错误响应
   */
  private void createOperationErrorResponse(Operation operation) {
    // 添加通用错误响应
    ApiResponse errorResponse = new ApiResponse()
        .description("Error Response")
        .content(new Content()
            .addMediaType("application/json",
                new MediaType().schema(new Schema<>()
                    .type("object")
                    .addProperty("code", new Schema<>().type("integer"))
                    .addProperty("message", new Schema<>().type("string")))));

    operation.getResponses().addApiResponse("400", errorResponse);
    operation.getResponses().addApiResponse("500", errorResponse);
  }

  /**
   * 示例注解
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  public @interface LargeFile {

    long maxSize() default 100 * 1024 * 1024; // 默认100MB
  }
}
