package org.example.type;

/**
 * Long数组处理器
 */
public class LongArrayTypeHandler extends BaseArrayTypeHandler<Long> {

  public LongArrayTypeHandler() {
    super(Long.class);
  }

  @Override
  protected String getTypeName() {
    return "bigint";
  }
}