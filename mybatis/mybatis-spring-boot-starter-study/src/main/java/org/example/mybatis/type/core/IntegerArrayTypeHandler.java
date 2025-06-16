package org.example.mybatis.type.core;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(Integer[].class)
@MappedJdbcTypes(value = JdbcType.ARRAY, includeNullJdbcType = true)
public class IntegerArrayTypeHandler extends AbstractArrayTypeHandler<Integer> {

  public IntegerArrayTypeHandler() {
    super(Integer.class);
  }

  @Override
  protected String getTypeName() {
    return JdbcType.INTEGER.name();
  }
}
