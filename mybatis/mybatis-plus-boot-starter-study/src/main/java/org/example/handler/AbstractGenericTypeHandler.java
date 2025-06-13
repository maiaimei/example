package org.example.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;

public abstract class AbstractGenericTypeHandler<T> extends BaseTypeHandler<T> {

  protected final Class<T> type;
  protected final ObjectMapper objectMapper;

  public AbstractGenericTypeHandler(Class<T> type) {
    this.type = type;
    this.objectMapper = new ObjectMapper();
    // 配置ObjectMapper
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  protected String toJsonString(T obj) throws JsonProcessingException {
    return obj != null ? objectMapper.writeValueAsString(obj) : null;
  }

  protected T fromJsonString(String json) throws JsonProcessingException {
    return json != null ? objectMapper.readValue(json, type) : null;
  }
}