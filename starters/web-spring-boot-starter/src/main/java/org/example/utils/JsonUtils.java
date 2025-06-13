package org.example.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.JsonConvertException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * JSON 工具类
 */
@Slf4j
@Component
public class JsonUtils {

  private static ObjectMapper objectMapper;

  public JsonUtils(ObjectMapper objectMapper) {
    JsonUtils.objectMapper = objectMapper;
  }

  /**
   * 对象转JSON字符串
   */
  public static String toJson(Object object) {
    if (object == null) {
      return null;
    }
    try {
      return JsonUtils.objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error("Convert object to json string error", e);
      throw new JsonConvertException("Convert object to json string error", e);
    }
  }

  /**
   * 对象转格式化的JSON字符串
   */
  public static String toPrettyJson(Object object) {
    if (object == null) {
      return null;
    }
    try {
      return JsonUtils.objectMapper.writerWithDefaultPrettyPrinter()
          .writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error("Convert object to pretty json string error", e);
      throw new JsonConvertException("Convert object to pretty json string error", e);
    }
  }

  /**
   * JSON字符串转对象
   */
  public static <T> T toObject(String json, Class<T> clazz) {
    if (!StringUtils.hasText(json)) {
      return null;
    }
    try {
      return JsonUtils.objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      log.error("Parse json to object error", e);
      throw new JsonConvertException("Parse json to object error", e);
    }
  }

  /**
   * JSON字符串转复杂对象
   */
  public static <T> T toObject(String json, TypeReference<T> typeReference) {
    if (!StringUtils.hasText(json)) {
      return null;
    }
    try {
      return JsonUtils.objectMapper.readValue(json, typeReference);
    } catch (JsonProcessingException e) {
      log.error("Parse json to complex object error", e);
      throw new JsonConvertException("Parse json to complex object error", e);
    }
  }

  /**
   * JSON字符串转对象列表
   */
  public static <T> List<T> toList(String json, Class<T> clazz) {
    if (!StringUtils.hasText(json)) {
      return new ArrayList<>();
    }
    try {
      return JsonUtils.objectMapper.readValue(json,
          JsonUtils.objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
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
    return JsonUtils.objectMapper.convertValue(object, new TypeReference<Map<String, Object>>() {
    });
  }

  /**
   * Map转对象
   */
  public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
    if (map == null) {
      return null;
    }
    return JsonUtils.objectMapper.convertValue(map, clazz);
  }

  /**
   * 深拷贝对象
   */
  public static <T> T deepCopy(T object, Class<T> clazz) {
    if (object == null) {
      return null;
    }
    return JsonUtils.objectMapper.convertValue(object, clazz);
  }

  /**
   * 格式化JSON字符串
   */
  public static String formatJson(String json) {
    if (!StringUtils.hasText(json)) {
      return json;
    }
    try {
      Object obj = JsonUtils.objectMapper.readValue(json, Object.class);
      return JsonUtils.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    } catch (IOException e) {
      log.error("Format json error", e);
      throw new JsonConvertException("Format json error", e);
    }
  }

  /**
   * 判断字符串是否为有效的JSON
   */
  public static boolean isValidJson(String json) {
    if (!StringUtils.hasText(json)) {
      return false;
    }
    try {
      JsonUtils.objectMapper.readTree(json);
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
      JsonNode node1 = JsonUtils.objectMapper.readTree(json1);
      JsonNode node2 = JsonUtils.objectMapper.readTree(json2);
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

}
