package org.example.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class CustomBooleanTypeHandler extends BaseTypeHandler<Boolean> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType) throws SQLException {
    // 确保 false 值被正确设置
    if (jdbcType == null) {
      ps.setBoolean(i, parameter);
    } else {
      ps.setObject(i, parameter, jdbcType.TYPE_CODE);
    }
  }

  @Override
  public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
    // 直接使用 getBoolean，避免字符串转换
    Object value = rs.getObject(columnName);
    switch (value) {
      case null -> {
        return null;
      }

      // 处理不同类型的返回值
      case Boolean b -> {
        return b;
      }
      case Number number -> {
        return number.intValue() != 0;
      }
      default -> {
      }
    }
    String strValue = value.toString().toLowerCase().trim();
    return parseBoolean(strValue);
  }

  @Override
  public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    Object value = rs.getObject(columnIndex);
    switch (value) {
      case null -> {
        return null;
      }
      case Boolean b -> {
        return b;
      }
      case Number number -> {
        return number.intValue() != 0;
      }
      default -> {
      }
    }
    String strValue = value.toString().toLowerCase().trim();
    return parseBoolean(strValue);
  }

  @Override
  public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    Object value = cs.getObject(columnIndex);
    switch (value) {
      case null -> {
        return null;
      }
      case Boolean b -> {
        return b;
      }
      case Number number -> {
        return number.intValue() != 0;
      }
      default -> {
      }
    }
    String strValue = value.toString().toLowerCase().trim();
    return parseBoolean(strValue);
  }

  private Boolean parseBoolean(String value) {
    if (value == null) {
      return null;
    }
    // 扩展判断条件，确保false值能被正确识别
    return switch (value.toLowerCase().trim()) {
      case "true", "1", "y", "yes", "on" -> true;
      case "false", "0", "n", "no", "off" -> false;
      default -> false; // 对于无法识别的值返回false
    };
  }
}

