package org.example.mybatis.typehandler;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * Custom TypeHandler for converting List<BigDecimal> or BigDecimal[] to database-specific types.
 */
@SuppressWarnings("unchecked")
public class BigDecimalArrayTypeHandler extends BaseTypeHandler<Object> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
    if (parameter instanceof List) {
      List<BigDecimal> bigDecimalList = (List<BigDecimal>) parameter;
      ps.setArray(i, ps.getConnection().createArrayOf("NUMERIC", bigDecimalList.toArray()));
    } else if (parameter instanceof BigDecimal[] bigDecimalArray) {
      ps.setArray(i, ps.getConnection().createArrayOf("NUMERIC", bigDecimalArray));
    } else {
      throw new IllegalArgumentException("Parameter must be List<BigDecimal> or BigDecimal[]");
    }
  }

  @Override
  public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
    Array array = rs.getArray(columnName);
    return convertArrayToBigDecimalList(array);
  }

  @Override
  public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    Array array = rs.getArray(columnIndex);
    return convertArrayToBigDecimalList(array);
  }

  @Override
  public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    Array array = cs.getArray(columnIndex);
    return convertArrayToBigDecimalList(array);
  }

  private List<BigDecimal> convertArrayToBigDecimalList(Array array) throws SQLException {
    if (array == null) {
      return null;
    }
    Object[] arrayData = (Object[]) array.getArray();
    List<BigDecimal> bigDecimalList = new ArrayList<>();
    for (Object obj : arrayData) {
      if (obj instanceof BigDecimal) {
        bigDecimalList.add((BigDecimal) obj);
      } else {
        bigDecimalList.add(new BigDecimal(obj.toString()));
      }
    }
    return bigDecimalList;
  }
}