package org.example.mybatis.type.core;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(Long[].class)
@MappedJdbcTypes(value = JdbcType.ARRAY, includeNullJdbcType = true)
public class LongArrayTypeHandler extends AbstractArrayTypeHandler<Long> {

  public LongArrayTypeHandler() {
    super(Long.class);
  }

  @Override
  protected String getTypeName() {
    return JdbcType.BIGINT.name();
  }
}
