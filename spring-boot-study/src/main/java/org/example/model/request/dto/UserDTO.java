package org.example.model.request.dto;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserDTO {

  private BigDecimal id;
  @NotBlank
  private String name;
  private String phone;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
