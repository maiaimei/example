package org.example.type;

import java.util.List;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * String列表处理器
 */
@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.ARRAY)
public class StringListTypeHandler extends BaseListTypeHandler<String> {

  public StringListTypeHandler() {
    super(String.class);
  }

  @Override
  protected String getTypeName() {
    return "varchar";
  }
}