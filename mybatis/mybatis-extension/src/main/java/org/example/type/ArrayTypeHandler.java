package org.example.type;

import java.sql.*;
import java.util.Objects;
import org.apache.ibatis.type.JdbcType;

@SuppressWarnings("unchecked")
public class ArrayTypeHandler<T> extends AbstractGenericTypeHandler<T[]> {

  public ArrayTypeHandler(Class<T> type) {
    super(type);
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, T[] parameter, JdbcType jdbcType) throws SQLException {
    if (Objects.nonNull(parameter)) {
      String arrayTypeName = resolveTypeName(type);
      ps.setArray(i, ps.getConnection().createArrayOf(arrayTypeName, parameter));
    } else {
      ps.setNull(i, Types.ARRAY);
    }
  }

  @Override
  public T[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
    Array array = rs.getArray(columnName);
    return (T[]) convertArrayToObject(array);
  }

  @Override
  public T[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    Array array = rs.getArray(columnIndex);
    return (T[]) convertArrayToObject(array);
  }

  @Override
  public T[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    Array array = cs.getArray(columnIndex);
    return (T[]) convertArrayToObject(array);
  }
}