package org.example.mybatis.type.core;

import java.sql.*;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

@Slf4j
public abstract class AbstractArrayTypeHandler<T> extends BaseTypeHandler<T[]> {

  private final Class<T> type;

  public AbstractArrayTypeHandler(Class<T> type) {
    this.type = type;
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, T[] parameter, JdbcType jdbcType)
      throws SQLException {
    if (Objects.isNull(parameter) || parameter.length == 0) {
      ps.setNull(i, Types.ARRAY);
      return;
    }

    Connection conn = ps.getConnection();
    Array array = null;
    try {
      array = conn.createArrayOf(getTypeName(), parameter);
      ps.setArray(i, array);
    } catch (SQLException e) {
      log.error("Error setting array parameter at index {}: {}", i, parameter, e);
      throw e;
    } finally {
      if (Objects.nonNull(array)) {
        try {
          // 释放 Array 对象，避免内存泄漏
          array.free();
        } catch (SQLException e) {
          log.error("Error freeing Array object", e);
        }
      }
    }
  }

  @Override
  public T[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
    try {
      return getArray(rs.getArray(columnName));
    } catch (SQLException e) {
      log.error("Error retrieving array from column '{}'", columnName, e);
      throw e;
    }
  }

  @Override
  public T[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    try {
      return getArray(rs.getArray(columnIndex));
    } catch (SQLException e) {
      log.error("Error retrieving array from column index {}", columnIndex, e);
      throw e;
    }
  }

  @Override
  public T[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    try {
      return getArray(cs.getArray(columnIndex));
    } catch (SQLException e) {
      log.error("Error retrieving array from callable statement at index {}", columnIndex, e);
      throw e;
    }
  }

  @SuppressWarnings("unchecked")
  private T[] getArray(Array array) throws SQLException {
    if (Objects.isNull(array)) {
      return null;
    }
    try {
      Object[] objArray = (Object[]) array.getArray();
      // 使用 type 成员变量进行类型检查
      for (Object obj : objArray) {
        if (!type.isInstance(obj)) {
          throw new SQLException("Array contains elements that are not of type: " + type.getName());
        }
      }
      return (T[]) objArray;
    } catch (ClassCastException e) {
      log.error("Error casting SQL array to generic array", e);
      throw new SQLException("Failed to cast SQL array to generic array", e);
    } finally {
      array.free();
    }
  }

  /**
   * Returns the SQL type name for the array elements.
   * Subclasses must implement this method to specify the type name.
   *
   * @return the SQL type name (e.g., "VARCHAR", "INTEGER").
   */
  protected abstract String getTypeName();
}
