package org.example.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.postgresql.util.PGobject;

@MappedJdbcTypes(JdbcType.OTHER)
public class JsonbTypeHandler<T> extends AbstractGenericTypeHandler<T> {

  public JsonbTypeHandler(Class<T> type) {
    super(type);
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType)
      throws SQLException {
    try {
      PGobject pgObject = new PGobject();
      pgObject.setType("jsonb");
      pgObject.setValue(toJsonString(parameter));
      ps.setObject(i, pgObject);
    } catch (JsonProcessingException e) {
      throw new SQLException("Error converting object to jsonb", e);
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
      throw new SQLException("Error parsing jsonb to object", e);
    }
  }
}