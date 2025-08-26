//package cn.maiaimei.advice;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertInstanceOf;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.net.URI;
//import lombok.extern.slf4j.Slf4j;
//import org.example.config.TestConfig;
//import org.example.controller.TestController;
//import cn.maiaimei.model.ApiResponse;
//import cn.maiaimei.model.SuccessApiResponse;
//import cn.maiaimei.model.TestDTO;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
//
//@Slf4j
//@WebMvcTest
//@ContextConfiguration(classes = {
//    GlobalResponseHandler.class,
//    TestConfig.class,
//    TestController.class
//})
//class GlobalResponseHandlerTest {
//
//  @Autowired
//  private ObjectMapper objectMapper;
//
//  @MockitoSpyBean
//  private GlobalResponseHandler globalResponseHandler;
//
//  @Test
//  void beforeBodyWrite_WithNull_ShouldWrapCorrectly() {
//    // Arrange
//    ServerHttpRequest request = createMockRequest();
//    ServerHttpResponse response = mock(ServerHttpResponse.class);
//    MethodParameter methodParameter = mock(MethodParameter.class);
//
//    // Act
//    Object result = globalResponseHandler.beforeBodyWrite(
//        null,
//        methodParameter,
//        MediaType.APPLICATION_JSON,
//        null,
//        request,
//        response
//    );
//
//    log.info("{}", result);
//
//    // Assert
//    assertInstanceOf(SuccessApiResponse.class, result);
//    SuccessApiResponse<?> apiResponse = (SuccessApiResponse<?>) result;
//    assertNull(apiResponse.getData());
//    assertEquals("/test", apiResponse.getPath());
//    assertEquals("GET", apiResponse.getMethod());
//
//    verify(request).getURI();
//    verify(request).getMethod();
//  }
//
//  @Test
//  void beforeBodyWrite_WithString_ShouldWrapAndSerializeCorrectly() throws Exception {
//    // Arrange
//    ServerHttpRequest request = createMockRequest();
//    ServerHttpResponse response = mock(ServerHttpResponse.class);
//    MethodParameter methodParameter = mock(MethodParameter.class);
//    String testData = "test message";
//
//    // Act
//    Object result = globalResponseHandler.beforeBodyWrite(
//        testData,
//        methodParameter,
//        MediaType.APPLICATION_JSON,
//        null,
//        request,
//        response
//    );
//
//    log.info("{}", result);
//
//    // Assert
//    assertInstanceOf(String.class, result);
//    SuccessApiResponse<?> deserializedResponse = objectMapper.readValue((String) result, SuccessApiResponse.class);
//    assertEquals(testData, deserializedResponse.getData());
//    assertEquals("/test", deserializedResponse.getPath());
//    assertEquals("GET", deserializedResponse.getMethod());
//
//    // Verify the request methods were called
//    verify(request).getURI();
//    verify(request).getMethod();
//  }
//
//  @Test
//  void beforeBodyWrite_WithBasicResponse_ShouldNotWrap() {
//    // Arrange
//    ServerHttpRequest request = createMockRequest();
//    ServerHttpResponse response = mock(ServerHttpResponse.class);
//    MethodParameter methodParameter = mock(MethodParameter.class);
//    SuccessApiResponse<String> originalResponse = ApiResponse.success("test data", "/test", "GET");
//
//    // Act
//    Object result = globalResponseHandler.beforeBodyWrite(
//        originalResponse,
//        methodParameter,
//        MediaType.APPLICATION_JSON,
//        null,
//        request,
//        response
//    );
//
//    log.info("{}", result);
//
//    // Assert
//    assertSame(originalResponse, result);
//  }
//
//  @Test
//  void beforeBodyWrite_WithObject_ShouldWrapCorrectly() {
//    // Arrange
//    ServerHttpRequest request = createMockRequest();
//    ServerHttpResponse response = mock(ServerHttpResponse.class);
//    MethodParameter methodParameter = mock(MethodParameter.class);
//    TestDTO testDTO = new TestDTO("test value");
//
//    // Act
//    Object result = globalResponseHandler.beforeBodyWrite(
//        testDTO,
//        methodParameter,
//        MediaType.APPLICATION_JSON,
//        null,
//        request,
//        response
//    );
//
//    log.info("{}", result);
//
//    // Assert
//    assertInstanceOf(SuccessApiResponse.class, result);
//    SuccessApiResponse<?> apiResponse = (SuccessApiResponse<?>) result;
//    assertEquals(testDTO, apiResponse.getData());
//    assertEquals("/test", apiResponse.getPath());
//    assertEquals("GET", apiResponse.getMethod());
//
//    verify(request).getURI();
//    verify(request).getMethod();
//  }
//
//  @Test
//  void supports_WithSkipResponseWrapper_ShouldReturnFalse() throws NoSuchMethodException {
//    // Arrange
//    MethodParameter methodParameter = new MethodParameter(
//        TestController.class.getMethod("skippedEndpoint"),
//        -1
//    );
//
//    // Act
//    boolean result = globalResponseHandler.supports(methodParameter, null);
//
//    // Assert
//    assertFalse(result);
//  }
//
//  @Test
//  void supports_WithNormalEndpoint_ShouldReturnTrue() throws NoSuchMethodException {
//    // Arrange
//    MethodParameter methodParameter = new MethodParameter(
//        TestController.class.getMethod("normalEndpoint"),
//        -1
//    );
//
//    // Act
//    boolean result = globalResponseHandler.supports(methodParameter, null);
//
//    // Assert
//    assertTrue(result);
//  }
//
//  private ServerHttpRequest createMockRequest() {
//    ServerHttpRequest request = mock(ServerHttpRequest.class);
//    when(request.getURI()).thenReturn(URI.create("/test"));
//    when(request.getMethod()).thenReturn(HttpMethod.GET);
//    return request;
//  }
//}
