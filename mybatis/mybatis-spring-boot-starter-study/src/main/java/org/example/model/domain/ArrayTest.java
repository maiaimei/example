package org.example.model.domain;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import org.example.mybatis.annotation.TableColumn;
import org.example.mybatis.annotation.TableName;
import org.example.mybatis.annotation.TypeHandler;
import org.example.type.BigDecimalListTypeHandler;
import org.example.type.IntegerListTypeHandler;
import org.example.type.StringListTypeHandler;

@Data
@TableName("ARRAY_TEST")
public class ArrayTest {

  private BigDecimal id;

  @TableColumn(type = "INTEGER[]")
  private Integer[] integerArray;

  @TableColumn(type = "NUMERIC(22)[]")
  private BigDecimal[] bigDecimalArray;

  @TableColumn(type = "VARCHAR[]")
  private String[] stringArray;

  @TableColumn(type = "INTEGER[]")
  @TypeHandler(IntegerListTypeHandler.class)
  private List<Integer> integerList;

  @TableColumn(type = "NUMERIC(22)[]")
  @TypeHandler(BigDecimalListTypeHandler.class)
  private List<BigDecimal> bigDecimalList;

  @TableColumn(type = "VARCHAR[]")
  @TypeHandler(StringListTypeHandler.class)
  private List<String> stringList;
}
