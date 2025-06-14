package org.example.handler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Array;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public abstract class AbstractGenericTypeHandler<T> extends BaseTypeHandler<T> {

  protected static final ConcurrentHashMap<Class<?>, String> STANDARD_MAPPING;

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

  protected final Class<?> type;

  protected AbstractGenericTypeHandler(Class<?> type) {
    if (type == null) {
      throw new IllegalArgumentException("type argument cannot be null");
    }
    this.type = type;
  }

  protected String resolveTypeName(Class<?> type) {
    return STANDARD_MAPPING.getOrDefault(type, JdbcType.JAVA_OBJECT.name());
  }

  protected Object[] convertArrayToObject(Array array) throws SQLException {
    if (Objects.isNull(array)) {
      return null;
    }
    Object result = array.getArray();
    array.free();
    return (Object[]) result;
  }
}