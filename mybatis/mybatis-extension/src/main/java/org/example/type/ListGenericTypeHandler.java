package org.example.type;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.apache.ibatis.type.JdbcType;

@SuppressWarnings("unchecked")
public class ListGenericTypeHandler<T> extends AbstractGenericTypeHandler<List<T>> {

  public ListGenericTypeHandler(Class<T> type) {
    super(type);
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, List<T> parameter, JdbcType jdbcType) throws SQLException {
    if (Objects.nonNull(parameter)) {
      String arrayTypeName = resolveTypeName(type);
      Object[] array = parameter.toArray();
      ps.setArray(i, ps.getConnection().createArrayOf(arrayTypeName, array));
    } else {
      ps.setNull(i, Types.ARRAY);
    }
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

  private List<T> convertArrayToList(Array array) throws SQLException {
    Object[] result = convertArrayToObject(array);
    return result == null ? null : Arrays.asList((T[]) result);
  }
}