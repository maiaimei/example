package cn.maiaimei.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Map.Entry;
import lombok.SneakyThrows;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class JsonUtils {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @SneakyThrows
  public static String stringify(Object value) {
    return OBJECT_MAPPER.writeValueAsString(value);
  }

  @SneakyThrows
  public static Map<String, Object> toMap(Object value) {
    return OBJECT_MAPPER.readValue(stringify(value), new TypeReference<>() {
    });
  }

  @SneakyThrows
  public static MultiValueMap<String, Object> toMultiValueMap(Object value) {
    final Map<String, Object> map = toMap(value);
    MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
    for (Entry<String, Object> entry : map.entrySet()) {
      multiValueMap.set(entry.getKey(), entry.getValue());
    }
    return multiValueMap;
  }

}
