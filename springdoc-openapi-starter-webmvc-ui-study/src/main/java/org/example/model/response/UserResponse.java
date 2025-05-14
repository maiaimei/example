package org.example.model.response;

import java.util.Set;
import lombok.Data;

@Data
public class UserResponse {

  private Long id;
  private String email;
  private String fullName;
  private int age;
  private Set<String> roles;
}

