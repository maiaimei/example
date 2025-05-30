package org.example.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class JacksonUtils {

  private static ObjectMapper objectMapper;

  public JacksonUtils(ObjectMapper objectMapper) {
    JacksonUtils.objectMapper = objectMapper;
  }

  public static String toJson(Object value) {
    try {
      return JacksonUtils.objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T toObject(String value, Class<T> valueType) {
    try {
      return JacksonUtils.objectMapper.readValue(value, valueType);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T toObject(String value, TypeReference<T> valueTypeRef) {
    try {
      return JacksonUtils.objectMapper.readValue(value, valueTypeRef);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> Map<String, T> toMap(String value) {
    try {
      return JacksonUtils.objectMapper.readValue(value, new TypeReference<>() {
      });
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
