package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.model.ErrorResponse;
import org.example.model.request.OrderRequest;
import org.example.model.response.OrderResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Error Responses and Examples
@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order Management")
public class OrderController {

  @Operation(
      summary = "Place new order",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Order created successfully",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = OrderResponse.class),
                  examples = @ExampleObject(
                      value = """
                          {
                              "orderId": "ORD-2023-001",
                              "totalAmount": 199.99,
                              "status": "CONFIRMED"
                          }
                          """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid input",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class),
                  examples = @ExampleObject(
                      value = """
                          {
                              "code": "INVALID_INPUT",
                              "message": "Invalid order details",
                              "details": ["Product quantity must be positive"]
                          }
                          """
                  )
              )
          )
      }
  )
  @PostMapping
  public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
    // Implementation
    return ResponseEntity.status(HttpStatus.CREATED).body(new OrderResponse());
  }
}
