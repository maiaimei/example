package org.example.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JacksonUtilsTest {

  @Mock
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    new JacksonUtils(objectMapper);
  }

  @Test
  void toJson_ShouldReturnJsonString() throws JsonProcessingException {
    // Arrange
    TestObject testObject = new TestObject("test", 123);
    String expectedJson = "{\"name\":\"test\",\"value\":123}";
    when(objectMapper.writeValueAsString(any())).thenReturn(expectedJson);

    // Act
    String result = JacksonUtils.toJson(testObject);

    // Assert
    assertEquals(expectedJson, result);
  }

  @Test
  void toJson_ShouldThrowRuntimeException_WhenJsonProcessingFails() throws JsonProcessingException {
    // Arrange
    TestObject testObject = new TestObject("test", 123);
    when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Error") {
    });

    // Act & Assert
    assertThrows(RuntimeException.class, () -> JacksonUtils.toJson(testObject));
  }

  @Test
  void toObject_ShouldReturnObject_WhenUsingClass() throws JsonProcessingException {
    // Arrange
    String json = "{\"name\":\"test\",\"value\":123}";
    TestObject expectedObject = new TestObject("test", 123);
    when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(expectedObject);

    // Act
    TestObject result = JacksonUtils.toObject(json, TestObject.class);

    // Assert
    assertNotNull(result);
    assertEquals(expectedObject.getName(), result.getName());
    assertEquals(expectedObject.getValue(), result.getValue());
  }

  @Test
  void toObject_ShouldReturnObject_WhenUsingTypeReference() throws JsonProcessingException {
    // Arrange
    String json = "{\"name\":\"test\",\"value\":123}";
    TestObject expectedObject = new TestObject("test", 123);
    when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(expectedObject);

    // Act
    TestObject result = JacksonUtils.toObject(json, new TypeReference<TestObject>() {
    });

    // Assert
    assertNotNull(result);
    assertEquals(expectedObject.getName(), result.getName());
    assertEquals(expectedObject.getValue(), result.getValue());
  }

  @Test
  void toMap_ShouldReturnMap_WhenValueIsString() throws JsonProcessingException {
    // Arrange
    String json = "{\"key\":\"value\"}";
    Map<String, String> expectedMap = new HashMap<>();
    expectedMap.put("key", "value");
    when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(expectedMap);

    // Act
    Map<String, String> result = JacksonUtils.toMap(json);

    // Assert
    assertNotNull(result);
    assertEquals(expectedMap, result);
  }

  @Test
  void toMap_ShouldReturnMap_WhenValueIsNonString() throws JsonProcessingException {
    // Arrange
    TestObject testObject = new TestObject("test", 123);
    final String testObjectJson = JacksonUtils.toJson(testObject);
    String json = "{\"key\":\"" + testObjectJson + "\"}";
    Map<String, TestObject> expectedMap = new HashMap<>();
    expectedMap.put("key", testObject);
    when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(expectedMap);

    // Act
    Map<String, TestObject> result = JacksonUtils.toMap(json);

    // Assert
    assertNotNull(result);
    assertEquals(expectedMap.get("key"), result.get("key"));
  }

  @Test
  void toMap_ShouldThrowRuntimeException_WhenJsonProcessingFails() throws JsonProcessingException {
    // Arrange
    String json = "{\"key\":\"value\"}";
    when(objectMapper.readValue(anyString(), any(TypeReference.class)))
        .thenThrow(new JsonProcessingException("Error") {
        });

    // Act & Assert
    assertThrows(RuntimeException.class, () -> JacksonUtils.toMap(json));
  }

  // 测试用的内部类
  private static class TestObject {

    private String name;
    private int value;

    public TestObject() {
    }

    public TestObject(String name, int value) {
      this.name = name;
      this.value = value;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getValue() {
      return value;
    }

    public void setValue(int value) {
      this.value = value;
    }
  }
}
