package org.example.mybatis.typehandler;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class BigDecimalListTypeHandler extends BaseTypeHandler<List<BigDecimal>> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, List<BigDecimal> parameter, JdbcType jdbcType) throws SQLException {
    ps.setString(i, BigDecimalTypeHandlerUtils.convertListToString(parameter));
  }

  @Override
  public List<BigDecimal> getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String value = rs.getString(columnName);
    return BigDecimalTypeHandlerUtils.parseBigDecimalList(value);
  }

  @Override
  public List<BigDecimal> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String value = rs.getString(columnIndex);
    return BigDecimalTypeHandlerUtils.parseBigDecimalList(value);
  }

  @Override
  public List<BigDecimal> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    String value = cs.getString(columnIndex);
    return BigDecimalTypeHandlerUtils.parseBigDecimalList(value);
  }
}