package org.example.type;

/**
 * Integer数组处理器
 */
public class IntegerArrayTypeHandler extends BaseArrayTypeHandler<Integer> {

  public IntegerArrayTypeHandler() {
    super(Integer.class);
  }

  @Override
  protected String getTypeName() {
    return "integer";
  }
}