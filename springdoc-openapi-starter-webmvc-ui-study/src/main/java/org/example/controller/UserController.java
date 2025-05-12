package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.model.ErrorResponse;
import org.example.model.UserRequest;
import org.example.model.UserResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Basic Controller with Various Annotations
@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "APIs for managing users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

  @Operation(
      summary = "Create new user",
      description = "Creates a new user with the provided data"
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "User created successfully",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = UserResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Invalid input",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  @PostMapping
  public ResponseEntity<UserResponse> createUser(
      @Parameter(description = "User details", required = true)
      @Valid @RequestBody UserRequest userRequest
  ) {
    // Implementation
    final UserResponse userResponse = new UserResponse();
    BeanUtils.copyProperties(userRequest, userResponse);
    return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
  }

  @Operation(
      summary = "Get user by ID",
      description = "Retrieves user details based on the provided ID"
  )
  @Parameters({
      @Parameter(
          name = "includeOrders",
          description = "Include user's order history",
          in = ParameterIn.QUERY,
          schema = @Schema(type = "boolean", defaultValue = "false")
      )
  })
  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUser(
      @Parameter(description = "User ID", required = true)
      @PathVariable Long id,
      @RequestParam(defaultValue = "false") boolean includeOrders
  ) {
    // Implementation
    return ResponseEntity.status(HttpStatus.OK).body(new UserResponse());
  }
}
