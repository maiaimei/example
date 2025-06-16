package org.example.mybatis.type.core;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(String[].class)
@MappedJdbcTypes(value = JdbcType.ARRAY, includeNullJdbcType = true)
public class StringArrayTypeHandler extends AbstractArrayTypeHandler<String> {

  public StringArrayTypeHandler() {
    super(String.class);
  }

  @Override
  protected String getTypeName() {
    return JdbcType.VARCHAR.name();
  }
}
