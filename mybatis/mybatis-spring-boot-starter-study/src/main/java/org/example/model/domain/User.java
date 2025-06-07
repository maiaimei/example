package org.example.model.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.example.mybatis.annotation.TableName;

@Data
@TableName("USER_TEST")
public class User {

  private BigDecimal id;
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private String email;
  private Boolean isEnabled;
  private Boolean isDeleted;
  private LocalDateTime createAt;
  private String createBy;
  private LocalDateTime updatedAt;
  private String updatedBy;
}
