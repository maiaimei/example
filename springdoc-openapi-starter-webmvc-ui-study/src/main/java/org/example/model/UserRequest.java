package org.example.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import lombok.Data;

@Data
@Schema(description = "User Request Model")
public class UserRequest {

  @Schema(description = "User ID", example = "1")
  private Long id;

  @Schema(
      description = "User's email address",
      example = "john.doe@example.com",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @Email
  private String email;

  @Schema(
      description = "User's full name",
      example = "John Doe",
      minLength = 2,
      maxLength = 100
  )
  @NotBlank
  private String fullName;

  @Schema(
      description = "User's age",
      example = "25",
      minimum = "18",
      maximum = "150"
  )
  @Min(18)
  private int age;

  @Schema(
      description = "User's roles",
      example = "[\"USER\", \"ADMIN\"]"
  )
  private Set<String> roles;
}
