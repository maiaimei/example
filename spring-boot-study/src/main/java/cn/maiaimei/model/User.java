package cn.maiaimei.model;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class User {

  private BigDecimal id;
  @NotBlank
  private String name;
  @NotBlank
  private String email;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
