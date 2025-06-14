package org.example.handler;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.apache.ibatis.type.JdbcType;

public class GenericArrayTypeHandler<T> extends AbstractGenericTypeHandler<T[]> {

  public GenericArrayTypeHandler(Class<T> type) {
    super(type);
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, T[] parameter, JdbcType jdbcType) throws SQLException {
    if (parameter != null) {
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