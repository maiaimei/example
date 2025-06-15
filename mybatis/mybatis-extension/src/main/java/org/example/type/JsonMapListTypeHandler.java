package org.example.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.*;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.type.*;

/**
 * JSON Map List类型处理器
 */
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR, JdbcType.OTHER})
public class JsonMapListTypeHandler extends BaseTypeHandler<List<Map<String, Object>>> {

  private final JavaType javaType;
  private final ObjectMapper objectMapper;

  public JsonMapListTypeHandler(ObjectMapper objectMapper) {
    if (objectMapper == null) {
      throw new IllegalArgumentException("ObjectMapper argument cannot be null");
    }
    this.objectMapper = objectMapper;
    this.javaType = objectMapper.getTypeFactory()
        .constructCollectionType(List.class,
            objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, List<Map<String, Object>> parameter, JdbcType jdbcType)
      throws SQLException {
    if (parameter == null) {
      ps.setNull(i, Types.OTHER);
      return;
    }
    try {
      String json = objectMapper.writeValueAsString(parameter);
      ps.setObject(i, json, Types.OTHER);
    } catch (JsonProcessingException e) {
      throw new SQLException("Failed to convert List<Map> to json", e);
    }
  }

  @Override
  public List<Map<String, Object>> getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return parse(rs.getString(columnName));
  }

  @Override
  public List<Map<String, Object>> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return parse(rs.getString(columnIndex));
  }

  @Override
  public List<Map<String, Object>> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return parse(cs.getString(columnIndex));
  }

  private List<Map<String, Object>> parse(String json) throws SQLException {
    if (json == null) {
      return null;
    }
    try {
      return objectMapper.readValue(json, javaType);
    } catch (JsonProcessingException e) {
      throw new SQLException("Failed to parse json to List<Map>: " + json, e);
    }
  }
}