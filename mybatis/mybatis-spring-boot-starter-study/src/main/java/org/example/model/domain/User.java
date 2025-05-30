package org.example.model.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.example.mybatis.annotation.TableName;

@Data
@TableName("SYS_USER")
public class User {

  private BigDecimal userId;
  private String nickname;
  private String username;
  private String password;
  private LocalDateTime gmtCreate;
  private LocalDateTime gmtModified;
}
