package org.example.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.*;
import java.util.List;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * JSON List类型处理器
 *
 * @param <T> 列表元素类型
 */
public class JsonListTypeHandler<T> extends BaseTypeHandler<List<T>> {

  private final JavaType javaType;
  private final ObjectMapper objectMapper;

  public JsonListTypeHandler(Class<T> type, ObjectMapper objectMapper) {
    if (type == null) {
      throw new IllegalArgumentException("Type argument cannot be null");
    }
    if (objectMapper == null) {
      throw new IllegalArgumentException("ObjectMapper argument cannot be null");
    }
    this.objectMapper = objectMapper;
    this.javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, type);
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, List<T> parameter, JdbcType jdbcType)
      throws SQLException {
    if (parameter == null) {
      ps.setNull(i, Types.OTHER);
      return;
    }
    try {
      String json = objectMapper.writeValueAsString(parameter);
      ps.setObject(i, json, Types.OTHER);
    } catch (JsonProcessingException e) {
      throw new SQLException("Failed to convert List to json", e);
    }
  }

  @Override
  public List<T> getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return parse(rs.getString(columnName));
  }

  @Override
  public List<T> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return parse(rs.getString(columnIndex));
  }

  @Override
  public List<T> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return parse(cs.getString(columnIndex));
  }

  private List<T> parse(String json) throws SQLException {
    if (json == null) {
      return null;
    }
    try {
      return objectMapper.readValue(json, javaType);
    } catch (JsonProcessingException e) {
      throw new SQLException("Failed to parse json array: " + json, e);
    }
  }
}