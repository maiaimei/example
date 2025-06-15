package org.example.type;

import java.math.BigDecimal;

/**
 * BigDecimal数组处理器
 */
public class BigDecimalArrayTypeHandler extends BaseArrayTypeHandler<BigDecimal> {

  public BigDecimalArrayTypeHandler() {
    super(BigDecimal.class);
  }

  @Override
  protected String getTypeName() {
    return "numeric";
  }
}