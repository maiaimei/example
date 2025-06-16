package org.example.model.request;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import org.example.model.domain.ConcreteElementA;
import org.example.model.domain.ConcreteElementB;

@Data
public class ConcreteDomainCreateOrUpdateRequest {

  private BigDecimal id;

  private Integer[] integers;

  private Long[] longs;

  private BigDecimal[] bigDecimals;

  private String[] strings;

  private ConcreteElementA concreteElementA;

  private List<ConcreteElementA> concreteElementAList;

  private ConcreteElementB concreteElementB;

  private List<ConcreteElementB> concreteElementBList;
}
