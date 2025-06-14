package org.example.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
@TableName("\"EXAMPLE_TEST\"")
public class Example {

  @TableId(value = "\"ID\"", type = IdType.INPUT)
  private BigDecimal id;

  @TableField(value = "\"NAME\"")
  private String name;

  @TableField(value = "\"INTEGER_LIST\"")
  private List<Integer> integerList;

  @TableField(value = "\"BIG_DECIMAL_LIST\"")
  private List<BigDecimal> bigDecimalList;

  @TableField(value = "\"STRING_LIST\"")
  private List<String> stringList;

  @TableField(value = "\"INTEGER_ARRAY\"")
  private Integer[] integerArray;

  @TableField(value = "\"BIG_DECIMAL_ARRAY\"")
  private BigDecimal[] bigDecimalArray;

  @TableField(value = "\"STRING_ARRAY\"")
  private String[] stringArray;

  @TableField(value = "\"CREATED_AT\"")
  private LocalDateTime createdAt;
}
