package org.example.mybatis.typehandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.example.utils.JsonUtils;

public class JsonTypeHandler<T> extends BaseTypeHandler<List<T>> {

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private final TypeReference<List<T>> typeReference;

  public JsonTypeHandler(TypeReference<List<T>> typeReference) {
    this.typeReference = typeReference;
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, List<T> parameter, JdbcType jdbcType) throws SQLException {
    ps.setString(i, JsonUtils.toJson(parameter)); // 将 List 转换为 JSON 字符串
  }

  @Override
  public List<T> getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String json = rs.getString(columnName); // 获取 JSON 字符串
    try {
      return JsonUtils.toObject(json, typeReference);
    } catch (Exception e) {
      throw new SQLException("Failed to parse JSON column", e);
    }
  }

  @Override
  public List<T> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String json = rs.getString(columnIndex); // 获取 JSON 字符串
    try {
      return JsonUtils.toObject(json, typeReference);
    } catch (Exception e) {
      throw new SQLException("Failed to parse JSON column", e);
    }
  }

  @Override
  public List<T> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    String json = cs.getString(columnIndex); // 获取 JSON 字符串
    try {
      return JsonUtils.toObject(json, typeReference);
    } catch (Exception e) {
      throw new SQLException("Failed to parse JSON column", e);
    }
  }
}