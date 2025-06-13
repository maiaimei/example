package org.example.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

@MappedJdbcTypes({JdbcType.VARCHAR, JdbcType.LONGVARCHAR})
public class TextTypeHandler<T> extends AbstractGenericTypeHandler<T> {

  public TextTypeHandler(Class<T> type) {
    super(type);
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType)
      throws SQLException {
    try {
      ps.setString(i, toJsonString(parameter));
    } catch (JsonProcessingException e) {
      throw new SQLException("Error converting object to string", e);
    }
  }

  @Override
  public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return parseResult(rs.getString(columnName));
  }

  @Override
  public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return parseResult(rs.getString(columnIndex));
  }

  @Override
  public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return parseResult(cs.getString(columnIndex));
  }

  private T parseResult(String value) throws SQLException {
    try {
      return fromJsonString(value);
    } catch (JsonProcessingException e) {
      throw new SQLException("Error parsing string to object", e);
    }
  }
}