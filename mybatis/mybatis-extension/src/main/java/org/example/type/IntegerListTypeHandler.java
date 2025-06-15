package org.example.type;

import java.util.List;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * Integer列表处理器
 */
@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.ARRAY)
public class IntegerListTypeHandler extends BaseListTypeHandler<Integer> {

  public IntegerListTypeHandler() {
    super(Integer.class);
  }

  @Override
  protected String getTypeName() {
    return "integer";
  }
}