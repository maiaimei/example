package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Controller with Complex Parameter Examples
@RestController
@RequestMapping("/products")
@Tag(name = "Product Management")
public class ProductController {

  @Operation(summary = "Search products")
  @Parameters({
      @Parameter(
          name = "category",
          description = "Product category",
          in = ParameterIn.QUERY,
          schema = @Schema(type = "string", allowableValues = {"ELECTRONICS", "BOOKS", "CLOTHING"})
      ),
      @Parameter(
          name = "priceRange",
          description = "Price range filter",
          in = ParameterIn.QUERY,
          array = @ArraySchema(schema = @Schema(type = "number")),
          example = "10,100"
      ),
      @Parameter(
          name = "X-API-Version",
          in = ParameterIn.HEADER,
          required = true,
          description = "API Version"
      )
  })
  @GetMapping("/search")
  public ResponseEntity<List<String>> searchProducts(
      @RequestParam(required = false) String category,
      @RequestParam(required = false) List<BigDecimal> priceRange,
      @RequestHeader("X-API-Version") String apiVersion) {
    // Implementation
    return ResponseEntity.status(HttpStatus.OK).body(List.of("P1"));
  }
}
