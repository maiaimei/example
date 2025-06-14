package org.example.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.example.utils.JsonUtils;

public class JsonTypeHandler<T> extends BaseTypeHandler<T> {
  
  private final Class<T> type;
  private final TypeReference<T> typeReference;

  // 构造方法：使用 Class 类型
  public JsonTypeHandler(Class<T> type) {
    if (type == null) {
      throw new IllegalArgumentException("Type argument cannot be null");
    }
    this.type = type;
    this.typeReference = null;
  }

  // 构造方法：使用 TypeReference 类型
  public JsonTypeHandler(TypeReference<T> typeReference) {
    if (typeReference == null) {
      throw new IllegalArgumentException("TypeReference argument cannot be null");
    }
    this.type = null;
    this.typeReference = typeReference;
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
    try {
      // 将 Java 对象序列化为 JSON 字符串
      String json = getObjectMapper().writeValueAsString(parameter);
      ps.setString(i, json);
    } catch (JsonProcessingException e) {
      throw new SQLException("Failed to convert parameter to JSON string: " + parameter, e);
    }
  }

  @Override
  public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String json = rs.getString(columnName);
    if (json == null || json.isEmpty()) {
      return null;
    }
    return parseJson(json);
  }

  @Override
  public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String json = rs.getString(columnIndex);
    if (json == null || json.isEmpty()) {
      return null;
    }
    return parseJson(json);
  }

  @Override
  public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    String json = cs.getString(columnIndex);
    if (json == null || json.isEmpty()) {
      return null;
    }
    return parseJson(json);
  }

  // 将 JSON 字符串反序列化为 Java 对象
  private T parseJson(String json) throws SQLException {
    try {
      if (type != null) {
        return getObjectMapper().readValue(json, type);
      } else if (typeReference != null) {
        return getObjectMapper().readValue(json, typeReference);
      } else {
        throw new IllegalStateException("Both type and typeReference are null");
      }
    } catch (JsonProcessingException e) {
      throw new SQLException("Failed to parse JSON string: " + json, e);
    }
  }

  private ObjectMapper getObjectMapper() {
    return JsonUtils.getObjectMapper();
  }
}