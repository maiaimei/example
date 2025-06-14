package org.example.model.domain;

import java.math.BigDecimal;
import lombok.Data;
import org.example.mybatis.annotation.TableColumn;
import org.example.mybatis.annotation.TableName;

@Data
@TableName("JSONB_TEST")
public class JsonbTest {

  private BigDecimal id;

  @TableColumn(type = "JSONB")
  private String data;
}
