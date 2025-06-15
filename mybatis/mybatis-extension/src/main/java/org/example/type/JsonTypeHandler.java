package org.example.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.*;
import java.util.Objects;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class JsonTypeHandler<T> extends BaseTypeHandler<T> {

  private final ObjectMapper objectMapper;
  private final Class<T> type;
  private final TypeReference<T> typeReference;

  public JsonTypeHandler(ObjectMapper objectMapper, Class<T> type) {
    this.objectMapper = objectMapper;
    this.type = type;
    this.typeReference = null;
  }

  public JsonTypeHandler(ObjectMapper objectMapper, TypeReference<T> typeReference) {
    this.objectMapper = objectMapper;
    this.type = null;
    this.typeReference = typeReference;
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType)
      throws SQLException {
    try {
      String json = objectMapper.writeValueAsString(parameter);
      ps.setString(i, json);
    } catch (JsonProcessingException e) {
      throw new SQLException("Error converting object to JSON", e);
    }
  }

  @Override
  public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String json = rs.getString(columnName);
    if (Objects.isNull(json)) {
      return null;
    }
    return parseJson(json);
  }

  @Override
  public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String json = rs.getString(columnIndex);
    if (Objects.isNull(json)) {
      return null;
    }
    return parseJson(json);
  }

  @Override
  public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    String json = cs.getString(columnIndex);
    if (Objects.isNull(json)) {
      return null;
    }
    return parseJson(json);
  }

  private T parseJson(String json) throws SQLException {
    try {
      if (Objects.nonNull(type)) {
        return objectMapper.readValue(json, type);
      }
      return objectMapper.readValue(json, typeReference);
    } catch (JsonProcessingException e) {
      throw new SQLException("Error converting JSON to object", e);
    }
  }
}
