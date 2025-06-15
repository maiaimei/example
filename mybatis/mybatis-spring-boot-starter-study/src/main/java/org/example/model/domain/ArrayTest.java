package org.example.model.domain;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import org.example.mybatis.annotation.TableColumn;
import org.example.mybatis.annotation.TableName;

@Data
@TableName("ARRAY_TEST")
public class ArrayTest {

  private BigDecimal id;

  @TableColumn(type = "INTEGER[]")
  private Integer[] integerArray;

  @TableColumn(type = "INTEGER[]")
  private List<Integer> integerList;

  @TableColumn(type = "NUMERIC(22)[]")
  private BigDecimal[] bigDecimalArray;

  @TableColumn(type = "NUMERIC(22)[]")
  private List<BigDecimal> bigDecimalList;

  @TableColumn(type = "VARCHAR[]")
  private String[] stringArray;

  @TableColumn(type = "VARCHAR[]")
  private List<String> stringList;
}
