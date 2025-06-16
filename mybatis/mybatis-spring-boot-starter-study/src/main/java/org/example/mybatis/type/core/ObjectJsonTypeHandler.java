package org.example.mybatis.type.core;

import java.sql.*;
import java.util.Objects;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.example.utils.JsonUtils;
import org.springframework.util.StringUtils;

/**
 * 对象形式的 JSON 类型处理器
 * 支持多种数据库的 JSON 类型转换
 */
public class ObjectJsonTypeHandler<T> extends BaseTypeHandler<T> {

  private final Class<T> type;

  public ObjectJsonTypeHandler(Class<T> type) {
    if (Objects.isNull(type)) {
      throw new IllegalArgumentException("Type argument cannot be null");
    }
    this.type = type;
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType)
      throws SQLException {
    if (Objects.isNull(parameter)) {
      ps.setNull(i, jdbcType == null ? Types.VARCHAR : jdbcType.TYPE_CODE);
      return;
    }
    String json = JsonUtils.toJson(parameter);
    ps.setString(i, json);
  }

  @Override
  public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return toObject(rs.getString(columnName));
  }

  @Override
  public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return toObject(rs.getString(columnIndex));
  }

  @Override
  public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return toObject(cs.getString(columnIndex));
  }

  private T toObject(String json) {
    if (!StringUtils.hasText(json)) {
      return null;
    }
    return JsonUtils.toObject(json, type);
  }
}

