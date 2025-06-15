package org.example.type;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * BigDecimal列表处理器
 */
@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.ARRAY)
public class BigDecimalListTypeHandler extends BaseListTypeHandler<BigDecimal> {

  public BigDecimalListTypeHandler() {
    super(BigDecimal.class);
  }

  @Override
  protected String getTypeName() {
    return "numeric";
  }
}