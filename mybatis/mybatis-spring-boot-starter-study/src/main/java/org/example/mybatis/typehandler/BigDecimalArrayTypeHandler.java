package org.example.mybatis.typehandler;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class BigDecimalArrayTypeHandler extends BaseTypeHandler<BigDecimal[]> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, BigDecimal[] parameter, JdbcType jdbcType) throws SQLException {
    ps.setString(i, BigDecimalTypeHandlerUtils.convertArrayToString(parameter));
  }

  @Override
  public BigDecimal[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String value = rs.getString(columnName);
    return BigDecimalTypeHandlerUtils.parseBigDecimalArray(value);
  }

  @Override
  public BigDecimal[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String value = rs.getString(columnIndex);
    return BigDecimalTypeHandlerUtils.parseBigDecimalArray(value);
  }

  @Override
  public BigDecimal[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    String value = cs.getString(columnIndex);
    return BigDecimalTypeHandlerUtils.parseBigDecimalArray(value);
  }
}