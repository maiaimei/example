package org.example.mybatis.type;

import java.math.BigDecimal;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(BigDecimal[].class)
@MappedJdbcTypes(value = JdbcType.ARRAY, includeNullJdbcType = true)
public class BigDecimalArrayTypeHandler extends AbstractGenericArrayTypeHandler<BigDecimal> {

  public BigDecimalArrayTypeHandler() {
    super(BigDecimal.class);
  }

  @Override
  protected String getTypeName() {
    return JdbcType.NUMERIC.name();
  }
}
