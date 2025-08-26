package org.example.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.JsonConvertException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * JSON 工具类
 */
@Slf4j
public final class JsonUtils {

  private static ObjectMapper objectMapper;

  private JsonUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * 对象转JSON字符串
   */
  public static String toJson(Object object) {
    if (object == null) {
      return null;
    }
    try {
      return getObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
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
      return getObjectMapper().writerWithDefaultPrettyPrinter()
          .writeValueAsString(object);
    } catch (JsonProcessingException e) {
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
      return getObjectMapper().readValue(json, clazz);
    } catch (JsonProcessingException e) {
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
      return getObjectMapper().readValue(json, typeReference);
    } catch (JsonProcessingException e) {
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
      return getObjectMapper().readValue(json,
          getObjectMapper().getTypeFactory().constructCollectionType(List.class, clazz));
    } catch (JsonProcessingException e) {
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
    return getObjectMapper().convertValue(object, new TypeReference<Map<String, Object>>() {
    });
  }

  /**
   * Map转对象
   */
  public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
    if (map == null) {
      return null;
    }
    return getObjectMapper().convertValue(map, clazz);
  }

  /**
   * 深拷贝对象
   */
  public static <T> T deepCopy(T object, Class<T> clazz) {
    if (object == null) {
      return null;
    }
    return getObjectMapper().convertValue(object, clazz);
  }

  /**
   * 格式化JSON字符串
   */
  public static String formatJson(String json) {
    if (!StringUtils.hasText(json)) {
      return json;
    }
    try {
      Object obj = getObjectMapper().readValue(json, Object.class);
      return getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    } catch (IOException e) {
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
      getObjectMapper().readTree(json);
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
      JsonNode node1 = getObjectMapper().readTree(json1);
      JsonNode node2 = getObjectMapper().readTree(json2);
      return merge(node1, node2);
    } catch (JsonProcessingException e) {
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

  public static void setObjectMapper(ObjectMapper objectMapper) {
    JsonUtils.objectMapper = objectMapper;
  }

  public static ObjectMapper getObjectMapper() {
    Assert.notNull(JsonUtils.objectMapper, "The objectMapper must not be null");
    return JsonUtils.objectMapper;
  }

  public static <T> T toObject(String json, JavaType javaType) {
    try {
      return getObjectMapper().readValue(json, javaType);
    } catch (JsonProcessingException e) {
      throw new JsonConvertException("JSON conversion failed", e);
    }
  }

  /**
   * 获取集合类型
   *
   * @param collectionClass 集合类 (如 ArrayList.class, HashSet.class)
   * @param elementClasses  元素类型 (如 String.class, Integer.class)
   * @return JavaType 集合类型
   */
  public static JavaType getCollectionType(Class<? extends Collection> collectionClass, Class<?>... elementClasses) {
    TypeFactory typeFactory = getObjectMapper().getTypeFactory();
    if (elementClasses.length == 1) {
      // 处理简单集合类型，如 List<String>
      return typeFactory.constructCollectionType(collectionClass, elementClasses[0]);
    } else {
      // 处理复杂类型，如 List<Map<String, Object>>
      JavaType[] javaTypes = new JavaType[elementClasses.length];
      for (int i = 0; i < elementClasses.length; i++) {
        javaTypes[i] = typeFactory.constructType(elementClasses[i]);
      }
      return typeFactory.constructParametricType(collectionClass, javaTypes);
    }
  }

  /**
   * 获取简单的List类型
   *
   * @param elementClass 元素类型
   * @return JavaType List类型
   */
  public static JavaType getListType(Class<?> elementClass) {
    return getObjectMapper().getTypeFactory().constructCollectionType(List.class, elementClass);
  }

  /**
   * 获取Map类型
   *
   * @param keyClass   键类型
   * @param valueClass 值类型
   * @return JavaType Map类型
   */
  public static JavaType getMapType(Class<?> keyClass, Class<?> valueClass) {
    return getObjectMapper().getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
  }

}
