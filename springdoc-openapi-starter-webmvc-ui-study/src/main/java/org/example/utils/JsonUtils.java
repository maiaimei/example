package org.example.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * JSON 工具类
 */
@Slf4j
public class JsonUtils {

  private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

  /**
   * 创建配置好的ObjectMapper
   */
  private static ObjectMapper createObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();

    // 配置序列化特性
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    // 配置反序列化特性
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

    // 注册Java8时间模块
    objectMapper.registerModule(new JavaTimeModule());

    return objectMapper;
  }

  /**
   * 获取ObjectMapper实例
   */
  public static ObjectMapper getObjectMapper() {
    return OBJECT_MAPPER;
  }

  /**
   * 对象转JSON字符串
   */
  public static String toJsonString(Object object) {
    if (object == null) {
      return null;
    }
    try {
      return OBJECT_MAPPER.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error("Convert object to json string error", e);
      throw new JsonConvertException("Convert object to json string error", e);
    }
  }

  /**
   * 对象转格式化的JSON字符串
   */
  public static String toPrettyJsonString(Object object) {
    if (object == null) {
      return null;
    }
    try {
      return OBJECT_MAPPER.writerWithDefaultPrettyPrinter()
          .writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error("Convert object to pretty json string error", e);
      throw new JsonConvertException("Convert object to pretty json string error", e);
    }
  }

  /**
   * JSON字符串转对象
   */
  public static <T> T parseObject(String json, Class<T> clazz) {
    if (!StringUtils.hasText(json)) {
      return null;
    }
    try {
      return OBJECT_MAPPER.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      log.error("Parse json to object error", e);
      throw new JsonConvertException("Parse json to object error", e);
    }
  }

  /**
   * JSON字符串转复杂对象
   */
  public static <T> T parseObject(String json, TypeReference<T> typeReference) {
    if (!StringUtils.hasText(json)) {
      return null;
    }
    try {
      return OBJECT_MAPPER.readValue(json, typeReference);
    } catch (JsonProcessingException e) {
      log.error("Parse json to complex object error", e);
      throw new JsonConvertException("Parse json to complex object error", e);
    }
  }

  /**
   * JSON字符串转对象列表
   */
  public static <T> List<T> parseArray(String json, Class<T> clazz) {
    if (!StringUtils.hasText(json)) {
      return new ArrayList<>();
    }
    try {
      return OBJECT_MAPPER.readValue(json,
          OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
    } catch (JsonProcessingException e) {
      log.error("Parse json to array error", e);
      throw new JsonConvertException("Parse json to array error", e);
    }
  }

  /**
   * 对象转Map
   */
  public static Map<String, Object> toMap(Object object) {
    if (object == null) {
      return null;
    }
    return OBJECT_MAPPER.convertValue(object, new TypeReference<Map<String, Object>>() {
    });
  }

  /**
   * Map转对象
   */
  public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
    if (map == null) {
      return null;
    }
    return OBJECT_MAPPER.convertValue(map, clazz);
  }

  /**
   * 深拷贝对象
   */
  public static <T> T deepCopy(T object, Class<T> clazz) {
    if (object == null) {
      return null;
    }
    return OBJECT_MAPPER.convertValue(object, clazz);
  }

  /**
   * 判断字符串是否为有效的JSON
   */
  public static boolean isValidJson(String json) {
    if (!StringUtils.hasText(json)) {
      return false;
    }
    try {
      OBJECT_MAPPER.readTree(json);
      return true;
    } catch (JsonProcessingException e) {
      return false;
    }
  }

  /**
   * 合并两个JSON对象
   */
  public static JsonNode merge(String json1, String json2) {
    try {
      JsonNode node1 = OBJECT_MAPPER.readTree(json1);
      JsonNode node2 = OBJECT_MAPPER.readTree(json2);
      return merge(node1, node2);
    } catch (JsonProcessingException e) {
      log.error("Merge json error", e);
      throw new JsonConvertException("Merge json error", e);
    }
  }

  /**
   * 合并两个JsonNode对象
   */
  private static JsonNode merge(JsonNode mainNode, JsonNode updateNode) {
    Iterator<String> fieldNames = updateNode.fieldNames();
    while (fieldNames.hasNext()) {
      String fieldName = fieldNames.next();
      JsonNode jsonNode = mainNode.get(fieldName);
      JsonNode updateValue = updateNode.get(fieldName);

      if (jsonNode != null && jsonNode.isObject()) {
        merge(jsonNode, updateValue);
      } else {
        if (mainNode instanceof ObjectNode) {
          ((ObjectNode) mainNode).replace(fieldName, updateValue);
        }
      }
    }
    return mainNode;
  }

  /**
   * 格式化JSON字符串
   */
  public static String formatJson(String json) {
    if (!StringUtils.hasText(json)) {
      return json;
    }
    try {
      Object obj = OBJECT_MAPPER.readValue(json, Object.class);
      return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    } catch (IOException e) {
      log.error("Format json error", e);
      throw new JsonConvertException("Format json error", e);
    }
  }

  /**
   * 自定义JSON转换异常
   */
  public static class JsonConvertException extends RuntimeException {

    public JsonConvertException(String message) {
      super(message);
    }

    public JsonConvertException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
