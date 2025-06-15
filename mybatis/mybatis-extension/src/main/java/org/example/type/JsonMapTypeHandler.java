package org.example.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.*;
import java.util.Map;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * JSON Map类型处理器
 */
public class JsonMapTypeHandler extends BaseTypeHandler<Map<String, Object>> {

  private final JavaType javaType;
  private final ObjectMapper objectMapper;

  public JsonMapTypeHandler(ObjectMapper objectMapper) {
    if (objectMapper == null) {
      throw new IllegalArgumentException("ObjectMapper argument cannot be null");
    }
    this.objectMapper = objectMapper;
    this.javaType = objectMapper.getTypeFactory()
        .constructMapType(Map.class, String.class, Object.class);
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, Map<String, Object> parameter, JdbcType jdbcType)
      throws SQLException {
    if (parameter == null) {
      ps.setNull(i, Types.OTHER);
      return;
    }
    try {
      String json = objectMapper.writeValueAsString(parameter);
      ps.setObject(i, json, Types.OTHER);
    } catch (JsonProcessingException e) {
      throw new SQLException("Failed to convert Map to json", e);
    }
  }

  @Override
  public Map<String, Object> getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return parse(rs.getString(columnName));
  }

  @Override
  public Map<String, Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return parse(rs.getString(columnIndex));
  }

  @Override
  public Map<String, Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return parse(cs.getString(columnIndex));
  }

  private Map<String, Object> parse(String json) throws SQLException {
    if (json == null) {
      return null;
    }
    try {
      return objectMapper.readValue(json, javaType);
    } catch (JsonProcessingException e) {
      throw new SQLException("Failed to parse json to Map: " + json, e);
    }
  }
}