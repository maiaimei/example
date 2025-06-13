package org.example.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

@MappedJdbcTypes(JdbcType.ARRAY)
public class ListTypeHandler<T> extends AbstractGenericTypeHandler<List<T>> {

  private final Class<T> elementType;

  public ListTypeHandler(Class<T> elementType) {
    super((Class<List<T>>) (Class<?>) List.class);
    this.elementType = elementType;
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, List<T> parameter, JdbcType jdbcType)
      throws SQLException {
    if (parameter == null) {
      ps.setNull(i, Types.ARRAY);
      return;
    }

    try {
      String[] stringArray = parameter.stream()
          .map(item -> {
            try {
              return objectMapper.writeValueAsString(item);
            } catch (JsonProcessingException e) {
              throw new RuntimeException(e);
            }
          })
          .toArray(String[]::new);

      Array array = ps.getConnection().createArrayOf("text", stringArray);
      ps.setArray(i, array);
    } catch (Exception e) {
      throw new SQLException("Error converting list to array", e);
    }
  }

  @Override
  public List<T> getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return parseArray(rs.getArray(columnName));
  }

  @Override
  public List<T> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return parseArray(rs.getArray(columnIndex));
  }

  @Override
  public List<T> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return parseArray(cs.getArray(columnIndex));
  }

  private List<T> parseArray(Array array) throws SQLException {
    if (array == null) {
      return null;
    }

    Object[] objArray = (Object[]) array.getArray();
    return Arrays.stream(objArray)
        .map(obj -> {
          try {
            return objectMapper.readValue(obj.toString(), elementType);
          } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
          }
        })
        .collect(Collectors.toList());
  }
}
