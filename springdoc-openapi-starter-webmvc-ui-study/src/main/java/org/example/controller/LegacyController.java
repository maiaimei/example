package org.example.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.model.UserProfile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Deprecated API Example
@RestController
@RequestMapping("/api/legacy")
@Tag(name = "Legacy APIs")
public class LegacyController {

  @Operation(
      summary = "Get user profile (Deprecated)",
      description = "This API is deprecated. Use /api/users/{id} instead"
  )
  @Deprecated
  @Hidden // Hides from OpenAPI documentation
  @GetMapping("/users/{id}")
  public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long id) {
    // Implementation
    return ResponseEntity.status(HttpStatus.OK).body(new UserProfile());
  }
}
