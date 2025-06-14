package org.example.handler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

@Slf4j
public class SqlArrayToListTypeHandler<T> extends BaseTypeHandler<List<T>> {

  private static final ConcurrentHashMap<Class<?>, String> STANDARD_MAPPING;

  static {
    STANDARD_MAPPING = new ConcurrentHashMap<>();
    STANDARD_MAPPING.put(BigDecimal.class, JdbcType.NUMERIC.name());
    STANDARD_MAPPING.put(BigInteger.class, JdbcType.BIGINT.name());
    STANDARD_MAPPING.put(boolean.class, JdbcType.BOOLEAN.name());
    STANDARD_MAPPING.put(Boolean.class, JdbcType.BOOLEAN.name());
    STANDARD_MAPPING.put(byte[].class, JdbcType.VARBINARY.name());
    STANDARD_MAPPING.put(byte.class, JdbcType.TINYINT.name());
    STANDARD_MAPPING.put(Byte.class, JdbcType.TINYINT.name());
    STANDARD_MAPPING.put(Calendar.class, JdbcType.TIMESTAMP.name());
    STANDARD_MAPPING.put(java.sql.Date.class, JdbcType.DATE.name());
    STANDARD_MAPPING.put(java.util.Date.class, JdbcType.TIMESTAMP.name());
    STANDARD_MAPPING.put(double.class, JdbcType.DOUBLE.name());
    STANDARD_MAPPING.put(Double.class, JdbcType.DOUBLE.name());
    STANDARD_MAPPING.put(float.class, JdbcType.REAL.name());
    STANDARD_MAPPING.put(Float.class, JdbcType.REAL.name());
    STANDARD_MAPPING.put(int.class, JdbcType.INTEGER.name());
    STANDARD_MAPPING.put(Integer.class, JdbcType.INTEGER.name());
    STANDARD_MAPPING.put(LocalDate.class, JdbcType.DATE.name());
    STANDARD_MAPPING.put(LocalDateTime.class, JdbcType.TIMESTAMP.name());
    STANDARD_MAPPING.put(LocalTime.class, JdbcType.TIME.name());
    STANDARD_MAPPING.put(long.class, JdbcType.BIGINT.name());
    STANDARD_MAPPING.put(Long.class, JdbcType.BIGINT.name());
    STANDARD_MAPPING.put(OffsetDateTime.class, JdbcType.TIMESTAMP_WITH_TIMEZONE.name());
    STANDARD_MAPPING.put(OffsetTime.class, JdbcType.TIME_WITH_TIMEZONE.name());
    STANDARD_MAPPING.put(Short.class, JdbcType.SMALLINT.name());
    STANDARD_MAPPING.put(String.class, JdbcType.VARCHAR.name());
    STANDARD_MAPPING.put(Time.class, JdbcType.TIME.name());
    STANDARD_MAPPING.put(Timestamp.class, JdbcType.TIMESTAMP.name());
    STANDARD_MAPPING.put(URL.class, JdbcType.DATALINK.name());
  }

  private final Class<T> javaType;

  public SqlArrayToListTypeHandler(Class<T> javaType) {
    if (javaType == null) {
      throw new IllegalArgumentException("javaType argument cannot be null");
    }
    this.javaType = javaType;
  }

  public SqlArrayToListTypeHandler(Class<T> javaType, String jdbcType) {
    if (javaType == null) {
      throw new IllegalArgumentException("javaType argument cannot be null");
    }
    if (jdbcType == null) {
      throw new IllegalArgumentException("jdbcType argument cannot be null");
    }
    this.javaType = javaType;
    STANDARD_MAPPING.putIfAbsent(javaType, jdbcType);
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, List<T> parameter, JdbcType jdbcType) throws SQLException {
    if (parameter != null) {
      // 将 List<T> 转换为数组并设置到 PreparedStatement
      String arrayTypeName = resolveTypeName(javaType);
      Object[] array = parameter.toArray();
      ps.setArray(i, ps.getConnection().createArrayOf(arrayTypeName, array));
    } else {
      ps.setNull(i, Types.ARRAY);
    }
  }

  protected String resolveTypeName(Class<?> type) {
    return STANDARD_MAPPING.getOrDefault(type, JdbcType.JAVA_OBJECT.name());
  }

  @Override
  public List<T> getNullableResult(ResultSet rs, String columnName) throws SQLException {
    Array array = rs.getArray(columnName);
    return convertArrayToList(array);
  }

  @Override
  public List<T> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    Array array = rs.getArray(columnIndex);
    return convertArrayToList(array);
  }

  @Override
  public List<T> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    Array array = cs.getArray(columnIndex);
    return convertArrayToList(array);
  }

  // 将 SQL Array 转换为 Java List<T>
  private List<T> convertArrayToList(Array array) throws SQLException {
    if (Objects.isNull(array)) {
      return null;
    }

    // 获取数组的实际数据库类型
    int baseType = array.getBaseType(); // 返回 java.sql.Types 的值
    String baseTypeName = array.getBaseTypeName(); // 返回数据库类型名称

    log.info("Array Base Type (SQL Type): {}", baseType);
    log.info("Array Base Type Name: {}", baseTypeName);

    // 将 SQL Array 转换为 Java 数组
    Object[] objectArray = (Object[]) array.getArray();

    // 将 Java 数组转换为 List<T>
    List<T> list = new ArrayList<>();
    for (Object obj : objectArray) {
      list.add(convertElement(obj, baseTypeName)); // 转换元素为基类型
    }
    return list;
  }

  // 根据基类型名称转换元素
  @SuppressWarnings("unchecked")
  private T convertElement(Object obj, String baseTypeName) {
    if (obj == null) {
      return null;
    }
    return switch (baseTypeName.toUpperCase()) {
      case "INTEGER" -> (T) Integer.valueOf(obj.toString());
      case "BIGINT" -> (T) Long.valueOf(obj.toString());
      case "NUMERIC", "DECIMAL" -> (T) new BigDecimal(obj.toString());
      case "DOUBLE" -> (T) Double.valueOf(obj.toString());
      case "REAL" -> (T) Float.valueOf(obj.toString());
      case "BOOLEAN" -> (T) Boolean.valueOf(obj.toString());
      case "VARCHAR", "TEXT" -> (T) obj.toString();
      case "DATE" -> (T) LocalDate.parse(obj.toString());
      case "TIMESTAMP" -> (T) LocalDateTime.parse(obj.toString());
      case "TIME" -> (T) LocalTime.parse(obj.toString());
      default -> throw new IllegalArgumentException("Unsupported base type: " + baseTypeName);
    };
  }
}