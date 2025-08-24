package org.example.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.example.properties.CustomFilterProperties;
import org.example.properties.RequestLoggingProperties;
import org.example.util.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

  private final List<String> excludePaths;
  private final long slowRequestThresholdMs;
  private final AntPathMatcher antPathMatcher;

  public RequestLoggingFilter(CustomFilterProperties customFilterProperties, RequestLoggingProperties requestLoggingProperties) {
    this.excludePaths = getExcludePaths(customFilterProperties, requestLoggingProperties);
    this.slowRequestThresholdMs = requestLoggingProperties.getSlowRequestThresholdMs();
    this.antPathMatcher = new AntPathMatcher();
  }

  private List<String> getExcludePaths(CustomFilterProperties customFilterProperties,
      RequestLoggingProperties requestLoggingProperties) {
    final List<String> paths = Optional.ofNullable(requestLoggingProperties.getExcludePaths()).orElseGet(ArrayList::new);
    if ("Y".equalsIgnoreCase(requestLoggingProperties.getUseCustomFilterExcludePaths())) {
      paths.addAll(Optional.ofNullable(customFilterProperties.getExcludePaths()).orElseGet(ArrayList::new));
    }
    return paths.stream().distinct().toList();
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    // 检查当前路径是否匹配任何排除路径
    return excludePaths.stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (isAsyncDispatch(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

    // 记录开始时间
    long startTime = System.currentTimeMillis();

    try {
      // 1. 记录请求开始的基本信息
      log.info("Started {} request to {}", request.getMethod(), request.getRequestURI());

      // 2. 先执行 filter chain，这样请求体才会被读取和缓存
      filterChain.doFilter(requestWrapper, responseWrapper);
    } finally {
      // 3. 在 filter chain 执行后记录请求和响应详细信息，此时才能获取到请求体
      log.info("Request details: {}", JsonUtils.toJson(collectRequestDetails(requestWrapper)));
      log.info("Response details: {}", JsonUtils.toJson(collectResponseDetails(responseWrapper)));

      // 4. 必须复制响应内容到原始响应对象
      responseWrapper.copyBodyToResponse();

      long duration = System.currentTimeMillis() - startTime;

      // 记录执行时间
      log.info("Ended {} request to {} - Status: {}, Execution time: {} ms",
          request.getMethod(), request.getRequestURI(), response.getStatus(), duration);

      // 对于较长时间的请求，可以添加警告日志
      if (duration > slowRequestThresholdMs) { // 5秒阈值可配置
        log.warn("Slow request detected! Execution time: {} ms - {} {}",
            duration, request.getMethod(), request.getRequestURI());
      }
    }
  }

  private Map<String, Object> collectRequestDetails(ContentCachingRequestWrapper request) throws IOException, ServletException {
    Map<String, Object> requestInfo = new HashMap<>();
    requestInfo.put("method", request.getMethod());
    requestInfo.put("path", request.getRequestURI());
    requestInfo.put("contentType", request.getContentType());
    requestInfo.put("headers", getRequestHeaders(request));
    appendQueryString(request, requestInfo);
    appendParameterMap(request, requestInfo);

    String contentType = request.getContentType();
    if (Objects.nonNull(contentType)) {
      if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
        appendRequestBody(request, requestInfo);
      } else if (contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
        appendMultipartInfo(request, requestInfo);
      }
    }
    return requestInfo;
  }

  private Map<String, Object> collectResponseDetails(ContentCachingResponseWrapper response) {
    Map<String, Object> responseInfo = new HashMap<>();
    responseInfo.put("status", response.getStatus());
    responseInfo.put("headers", getResponseHeaders(response));
    responseInfo.put("contentType", response.getContentType());

    if (shouldLogResponseBody(response.getContentType())) {
      responseInfo.put("body", getResponseBody(response));
    } else {
      responseInfo.put("body", "[BINARY CONTENT OR FILE - NOT LOGGED]");
    }
    return responseInfo;
  }

  private Map<String, String> getRequestHeaders(HttpServletRequest request) {
    return Collections.list(request.getHeaderNames())
        .stream()
        .collect(Collectors.toMap(
            headerName -> headerName,
            request::getHeader,
            (v1, v2) -> v1
        ));
  }

  private Map<String, String> getResponseHeaders(HttpServletResponse response) {
    return response.getHeaderNames()
        .stream()
        .collect(Collectors.toMap(
            headerName -> headerName,
            response::getHeader,
            (v1, v2) -> v1
        ));
  }

  private void appendQueryString(ContentCachingRequestWrapper request, Map<String, Object> requestInfo) {
    final String queryString = request.getQueryString();
    if (StringUtils.hasText(queryString)) {
      requestInfo.put("queryString", queryString);
    }
  }

  private void appendParameterMap(ContentCachingRequestWrapper request, Map<String, Object> requestInfo) {
    final Map<String, String[]> parameterMap = request.getParameterMap();
    if (!CollectionUtils.isEmpty(parameterMap)) {
      requestInfo.put("parameters", parameterMap);
    }
  }

  private void appendRequestBody(ContentCachingRequestWrapper request, Map<String, Object> requestInfo) {
    try {
      byte[] content = request.getContentAsByteArray();
      if (content.length > 0) {
        String bodyContent = new String(content, request.getCharacterEncoding());
        Map<String, Object> requestBody = JsonUtils.toObject(bodyContent, new TypeReference<>() {
        });
        requestInfo.put("body", requestBody);
      }
    } catch (Exception e) {
      log.error("Error parsing request body", e);
    }
  }

  private void appendMultipartInfo(HttpServletRequest request, Map<String, Object> requestInfo)
      throws IOException, ServletException {
    Map<String, Object> multipartInfo = new HashMap<>();
    Collection<Part> parts = request.getParts();

    for (Part part : parts) {
      Map<String, Object> partInfo = new HashMap<>();
      partInfo.put("name", part.getName());
      partInfo.put("contentType", part.getContentType());
      partInfo.put("size", part.getSize());

      if (Objects.nonNull(part.getSubmittedFileName())) {
        // 文件类型的 part
        partInfo.put("filename", part.getSubmittedFileName());
      } else {
        // 非文件类型的 part，读取内容
        String value = new String(part.getInputStream().readAllBytes(), request.getCharacterEncoding());
        partInfo.put("value", value);
      }

      multipartInfo.put(part.getName(), partInfo);
    }
    if (!CollectionUtils.isEmpty(multipartInfo)) {
      requestInfo.put("multipart", multipartInfo);
    }
  }

  private boolean shouldLogResponseBody(String contentType) {
    return StringUtils.hasText(contentType) && (
        contentType.contains(MediaType.APPLICATION_JSON_VALUE) ||
            contentType.contains(MediaType.APPLICATION_PROBLEM_JSON_VALUE) ||
            contentType.contains(MediaType.APPLICATION_XML_VALUE) ||
            contentType.contains(MediaType.TEXT_PLAIN_VALUE) ||
            contentType.contains(MediaType.TEXT_XML_VALUE) ||
            contentType.contains(MediaType.TEXT_HTML_VALUE)
    );
  }

  private Object getResponseBody(ContentCachingResponseWrapper response) {
    try {
      byte[] content = response.getContentAsByteArray();
      if (content.length > 0) {
        String bodyContent = new String(content, response.getCharacterEncoding());
        String contentType = response.getContentType();
        if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
          return JsonUtils.toObject(bodyContent, new TypeReference<>() {
          });
        }
        return bodyContent;
      }
    } catch (Exception e) {
      log.error("Error reading response body", e);
    }
    return null;
  }

}

