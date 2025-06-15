package org.example.type;

import java.util.List;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * Long列表处理器
 */
@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.ARRAY)
public class LongListTypeHandler extends BaseListTypeHandler<Long> {

  public LongListTypeHandler() {
    super(Long.class);
  }

  @Override
  protected String getTypeName() {
    return "bigint";
  }
}