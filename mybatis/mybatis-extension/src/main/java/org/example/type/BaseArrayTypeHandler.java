package org.example.type;

import java.sql.*;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 数组类型处理器基类
 */
public abstract class BaseArrayTypeHandler<T> extends BaseTypeHandler<T[]> {

  protected final Class<T> type;

  public BaseArrayTypeHandler(Class<T> type) {
    this.type = type;
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, T[] parameter, JdbcType jdbcType)
      throws SQLException {
    Connection conn = ps.getConnection();
    Array array = conn.createArrayOf(getTypeName(), parameter);
    ps.setArray(i, array);
  }

  @Override
  public T[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return getArray(rs.getArray(columnName));
  }

  @Override
  public T[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return getArray(rs.getArray(columnIndex));
  }

  @Override
  public T[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return getArray(cs.getArray(columnIndex));
  }

  protected T[] getArray(Array array) throws SQLException {
    if (array == null) {
      return null;
    }
    return (T[]) array.getArray();
  }

  protected abstract String getTypeName();
}