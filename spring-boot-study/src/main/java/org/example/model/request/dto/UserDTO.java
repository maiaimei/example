package org.example.model.request.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class UserDTO {

  private BigDecimal id;
  private String name;
  private String phone;
}
