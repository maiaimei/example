package org.example.type;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;

/**
 * 列表类型处理器基类
 */
public abstract class BaseListTypeHandler<T> extends BaseTypeHandler<List<T>> {

  protected final Class<T> type;

  public BaseListTypeHandler(Class<T> type) {
    this.type = type;
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, List<T> parameter, JdbcType jdbcType)
      throws SQLException {
    if (parameter == null) {
      ps.setNull(i, Types.ARRAY);
      return;
    }

    Connection conn = ps.getConnection();

    // 创建与类型匹配的数组
    Object[] array = (Object[]) java.lang.reflect.Array.newInstance(type, parameter.size());
    for (int j = 0; j < parameter.size(); j++) {
      array[j] = parameter.get(j);
    }

    Array sqlArray = conn.createArrayOf(getTypeName(), array);
    ps.setArray(i, sqlArray);
  }

  @Override
  public List<T> getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return arrayToList(rs.getArray(columnName));
  }

  @Override
  public List<T> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return arrayToList(rs.getArray(columnIndex));
  }

  @Override
  public List<T> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return arrayToList(cs.getArray(columnIndex));
  }

  protected List<T> arrayToList(Array array) throws SQLException {
    if (array == null) {
      return null;
    }

    Object[] objArray = (Object[]) array.getArray();
    List<T> list = new ArrayList<>(objArray.length);
    for (Object obj : objArray) {
      list.add(convertValue(obj));
    }
    return list;
  }

  @SuppressWarnings("unchecked")
  protected T convertValue(Object value) {
    switch (value) {
      case null -> {
        return null;
      }

      // 处理数值类型的转换
      case Number number when type == Long.class -> {
        return (T) Long.valueOf(number.longValue());
      }
      case Number number when type == Integer.class -> {
        return (T) Integer.valueOf(number.intValue());
      }
      case Number number when type == BigDecimal.class -> {
        return (T) BigDecimal.valueOf(number.doubleValue());
      }
      case Number number when type == Double.class -> {
        return (T) Double.valueOf(number.doubleValue());
      }
      case Number number when type == Float.class -> {
        return (T) Float.valueOf(number.floatValue());
      }
      default -> {
      }
    }

    // 处理字符串类型
    if (type == String.class && !(value instanceof String)) {
      return (T) String.valueOf(value);
    }

    // 如果类型已经匹配，直接转换
    if (type.isInstance(value)) {
      return type.cast(value);
    }

    throw new TypeException("Cannot convert " + value.getClass().getName() + " to " + type.getName());
  }

  protected abstract String getTypeName();
}