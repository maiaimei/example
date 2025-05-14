package org.example.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

// 示例模型类
@Data
public class UserReq {

  @NotNull
  private Long id;

  @NotNull
  @Email
  @Size(min = 5, max = 100)
  private String email;

  @NotNull
  @Size(min = 2, max = 100)
  private String fullName;

  @Min(18)
  @Max(150)
  private int age;

  @NotNull
  private Set<String> roles;

  @Pattern(regexp = "^\\+[1-9]\\d{1,14}$")
  private String phoneNumber;

  private LocalDateTime createdAt;

  @Size(max = 1000)
  private String description;
}