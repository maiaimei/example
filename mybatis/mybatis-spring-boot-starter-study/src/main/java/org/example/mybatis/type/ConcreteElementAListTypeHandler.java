package org.example.mybatis.type;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.example.model.domain.ConcreteElementA;
import org.example.model.domain.ConcreteElementAList;
import org.example.mybatis.type.core.AbstractArrayJsonTypeHandler;

@MappedTypes(ConcreteElementAList.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR, includeNullJdbcType = true)
public class ConcreteElementAListTypeHandler extends AbstractArrayJsonTypeHandler<ConcreteElementAList, ConcreteElementA> {

  public ConcreteElementAListTypeHandler() {
    super(ConcreteElementAList.class);
  }
}
