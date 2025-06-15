package org.example.type;

/**
 * String数组处理器
 */
public class StringArrayTypeHandler extends BaseArrayTypeHandler<String> {

  public StringArrayTypeHandler() {
    super(String.class);
  }

  @Override
  protected String getTypeName() {
    return "varchar";
  }
}