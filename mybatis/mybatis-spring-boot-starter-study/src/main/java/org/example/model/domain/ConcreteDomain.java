package org.example.model.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.example.mybatis.annotation.TableColumn;
import org.example.mybatis.annotation.TableName;

@Data
@TableName("CONCRETE_DOMAIN")
public class ConcreteDomain {

  private BigDecimal id;

  @TableColumn(type = "INTEGER[]")
  private Integer[] integers;

  @TableColumn(type = "BIGINT[]")
  private Long[] longs;

  @TableColumn(type = "NUMERIC(22)[]")
  private BigDecimal[] bigDecimals;

  @TableColumn(type = "TEXT[]")
  private String[] strings;

  @TableColumn(value = "CONCRETE_ELEMENT_A", type = "JSONB")
  private ConcreteElementA concreteElementA;

  @TableColumn(value = "CONCRETE_ELEMENT_A_LIST", type = "JSONB")
  private ConcreteElementAList concreteElementAList;

  @TableColumn(value = "CONCRETE_ELEMENT_B", type = "JSONB")
  private ConcreteElementB concreteElementB;

  @TableColumn(value = "CONCRETE_ELEMENT_B_LIST", type = "JSONB")
  private ConcreteElementBList concreteElementBList;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
