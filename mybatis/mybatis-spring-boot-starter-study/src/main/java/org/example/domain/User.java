package org.example.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class User {

  private BigDecimal userId;
  private String nickname;
  private String username;
  private String password;
  private LocalDateTime gmtCreate;
  private LocalDateTime gmtModified;
}
