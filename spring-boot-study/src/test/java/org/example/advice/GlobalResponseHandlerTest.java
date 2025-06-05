package org.example.advice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import lombok.Getter;
import org.example.advice.GlobalResponseHandlerTest.TestConfig;
import org.example.annotation.SkipResponseWrapper;
import org.example.config.JacksonConfig;
import org.example.model.response.ApiResponse;
import org.example.model.response.ApiResponse.SuccessResponse;
import org.example.utils.IdGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@WebMvcTest
@ContextConfiguration(classes = {
    TestConfig.class,
    GlobalResponseHandler.class,
    GlobalResponseHandlerTest.TestController.class
})
class GlobalResponseHandlerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoSpyBean
  private GlobalResponseHandler globalResponseHandler;

  @Test
  void beforeBodyWrite_WithString_ShouldWrapAndSerializeCorrectly() throws Exception {
    // Arrange
    ServerHttpRequest request = createMockRequest();
    ServerHttpResponse response = mock(ServerHttpResponse.class);
    MethodParameter methodParameter = mock(MethodParameter.class);
    String testData = "test message";

    // Act
    Object result = globalResponseHandler.beforeBodyWrite(
        testData,
        methodParameter,
        MediaType.APPLICATION_JSON,
        null,
        request,
        response
    );

    // Assert
    assertInstanceOf(String.class, result);
    SuccessResponse<?> deserializedResponse = objectMapper.readValue((String) result, SuccessResponse.class);
    assertEquals(testData, deserializedResponse.getData());
    assertEquals("/test", deserializedResponse.getPath());
    assertEquals("GET", deserializedResponse.getMethod());

    // Verify the request methods were called
    verify(request).getURI();
    verify(request).getMethod();
  }

  @Test
  void beforeBodyWrite_WithObject_ShouldWrapCorrectly() {
    // Arrange
    ServerHttpRequest request = createMockRequest();
    ServerHttpResponse response = mock(ServerHttpResponse.class);
    MethodParameter methodParameter = mock(MethodParameter.class);
    TestDto testDto = new TestDto("test value");

    // Act
    Object result = globalResponseHandler.beforeBodyWrite(
        testDto,
        methodParameter,
        MediaType.APPLICATION_JSON,
        null,
        request,
        response
    );

    // Assert
    assertInstanceOf(SuccessResponse.class, result);
    SuccessResponse<?> apiResponse = (SuccessResponse<?>) result;
    assertEquals(testDto, apiResponse.getData());
    assertEquals("/test", apiResponse.getPath());
    assertEquals("GET", apiResponse.getMethod());

    verify(request).getURI();
    verify(request).getMethod();
  }

  @Test
  void beforeBodyWrite_WithNull_ShouldWrapCorrectly() {
    // Arrange
    ServerHttpRequest request = createMockRequest();
    ServerHttpResponse response = mock(ServerHttpResponse.class);
    MethodParameter methodParameter = mock(MethodParameter.class);

    // Act
    Object result = globalResponseHandler.beforeBodyWrite(
        null,
        methodParameter,
        MediaType.APPLICATION_JSON,
        null,
        request,
        response
    );

    // Assert
    assertInstanceOf(SuccessResponse.class, result);
    SuccessResponse<?> apiResponse = (SuccessResponse<?>) result;
    assertNull(apiResponse.getData());
    assertEquals("/test", apiResponse.getPath());
    assertEquals("GET", apiResponse.getMethod());

    verify(request).getURI();
    verify(request).getMethod();
  }

  @Test
  void beforeBodyWrite_WithBasicResponse_ShouldNotWrap() {
    // Arrange
    ServerHttpRequest request = createMockRequest();
    ServerHttpResponse response = mock(ServerHttpResponse.class);
    MethodParameter methodParameter = mock(MethodParameter.class);
    SuccessResponse<String> originalResponse = ApiResponse.success("test data", "/test", "GET");

    // Act
    Object result = globalResponseHandler.beforeBodyWrite(
        originalResponse,
        methodParameter,
        MediaType.APPLICATION_JSON,
        null,
        request,
        response
    );

    // Assert
    assertSame(originalResponse, result);
  }

  @Test
  void supports_WithSkipResponseWrapper_ShouldReturnFalse() throws NoSuchMethodException {
    // Arrange
    MethodParameter methodParameter = new MethodParameter(
        TestController.class.getMethod("skippedEndpoint"),
        -1
    );

    // Act
    boolean result = globalResponseHandler.supports(methodParameter, null);

    // Assert
    assertFalse(result);
  }

  @Test
  void supports_WithNormalEndpoint_ShouldReturnTrue() throws NoSuchMethodException {
    // Arrange
    MethodParameter methodParameter = new MethodParameter(
        TestController.class.getMethod("normalEndpoint"),
        -1
    );

    // Act
    boolean result = globalResponseHandler.supports(methodParameter, null);

    // Assert
    assertTrue(result);
  }

  private ServerHttpRequest createMockRequest() {
    ServerHttpRequest request = mock(ServerHttpRequest.class);
    when(request.getURI()).thenReturn(URI.create("/test"));
    when(request.getMethod()).thenReturn(HttpMethod.GET);
    return request;
  }

  @TestConfiguration
  @Import(JacksonConfig.class)
  static class TestConfig {

  }

  // Test helper classes
  @RestController
  static class TestController {

    @SkipResponseWrapper
    @GetMapping("/skip")
    public String skippedEndpoint() {
      return "skipped";
    }

    @GetMapping("/normal")
    public String normalEndpoint() {
      return "normal";
    }
  }

  @Getter
  static class TestDto {

    private final BigDecimal id;
    private final String value;
    private final LocalDateTime createdAt;

    TestDto(String value) {
      this.id = IdGenerator.nextId();
      this.value = value;
      this.createdAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null || getClass() != obj.getClass()) {
        return false;
      }
      TestDto testDto = (TestDto) obj;
      return value.equals(testDto.value);
    }

    @Override
    public int hashCode() {
      return value.hashCode();
    }
  }
}
