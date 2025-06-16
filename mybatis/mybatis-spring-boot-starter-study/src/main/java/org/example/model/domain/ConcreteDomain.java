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

  @TableColumn(type = "NUMERIC(22)[]")
  private BigDecimal[] bigDecimals;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
