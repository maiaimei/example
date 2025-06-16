package org.example.mybatis.type;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.example.model.domain.ConcreteElementB;
import org.example.model.domain.ConcreteElementBList;
import org.example.mybatis.type.core.AbstractArrayJsonTypeHandler;

@MappedTypes(ConcreteElementBList.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR, includeNullJdbcType = true)
public class ConcreteElementBListTypeHandler extends AbstractArrayJsonTypeHandler<ConcreteElementBList, ConcreteElementB> {

  public ConcreteElementBListTypeHandler() {
    super(ConcreteElementBList.class);
  }
}
