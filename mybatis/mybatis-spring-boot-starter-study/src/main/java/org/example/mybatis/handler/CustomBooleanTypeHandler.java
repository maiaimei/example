package org.example.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class CustomBooleanTypeHandler extends BaseTypeHandler<Boolean> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType) throws SQLException {
    ps.setString(i, parameter ? "1" : "0");
  }

  @Override
  public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String value = rs.getString(columnName);
    return parseBoolean(value);
  }

  @Override
  public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String value = rs.getString(columnIndex);
    return parseBoolean(value);
  }

  @Override
  public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    String value = cs.getString(columnIndex);
    return parseBoolean(value);
  }

  private Boolean parseBoolean(String value) {
    if (value == null) {
      return null;
    }
    return "Y".equalsIgnoreCase(value) || "1".equals(value) || "true".equalsIgnoreCase(value);
  }
}
