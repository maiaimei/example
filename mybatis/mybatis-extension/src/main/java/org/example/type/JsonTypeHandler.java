package org.example.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.*;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * JSON类型处理器
 *
 * @param <T> 目标类型
 */
public class JsonTypeHandler<T> extends BaseTypeHandler<T> {

  private final Class<T> type;
  private final ObjectMapper objectMapper;

  public JsonTypeHandler(Class<T> type, ObjectMapper objectMapper) {
    if (type == null) {
      throw new IllegalArgumentException("Type argument cannot be null");
    }
    if (objectMapper == null) {
      throw new IllegalArgumentException("ObjectMapper argument cannot be null");
    }
    this.type = type;
    this.objectMapper = objectMapper;
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType)
      throws SQLException {
    if (parameter == null) {
      ps.setNull(i, Types.OTHER);
      return;
    }
    try {
      String json = objectMapper.writeValueAsString(parameter);
      ps.setObject(i, json, Types.OTHER);
    } catch (JsonProcessingException e) {
      throw new SQLException("Failed to convert object to json", e);
    }
  }

  @Override
  public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return parse(rs.getString(columnName));
  }

  @Override
  public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return parse(rs.getString(columnIndex));
  }

  @Override
  public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return parse(cs.getString(columnIndex));
  }

  private T parse(String json) throws SQLException {
    if (json == null) {
      return null;
    }
    try {
      return objectMapper.readValue(json, type);
    } catch (JsonProcessingException e) {
      throw new SQLException("Failed to parse json: " + json, e);
    }
  }
}
