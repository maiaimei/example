package cn.maiaimei.example.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

  private Long id;
  private String nickname;
  private String username;
  private String password;
  private Integer isEnabled;
  private Integer isDeleted;
  private LocalDateTime gmtCreate;
  private LocalDateTime gmtModified;
}
