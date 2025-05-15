package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.model.ApiRequest;
import org.example.model.ApiResponse;
import org.example.model.request.ExamplePageQueryRequest;
import org.example.model.request.ExampleRequest;
import org.example.model.response.ExampleResponse;
import org.example.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BasicExampleController", description = "This is description of BasicExampleController")
@RestController
@RequestMapping("/examples")
public class BasicExampleController {

  private static final List<ExampleResponse> exampleResponseList = new ArrayList<>();

  @Operation(summary = "Get an example response", description = "This is description of getting an example response")
  @GetMapping("/{id}")
  public ResponseEntity<?> get(@PathVariable BigDecimal id) {
    final Optional<ExampleResponse> optional = exampleResponseList.stream()
        .filter(exampleResponse -> exampleResponse.getBigDecimalField().equals(id)).findFirst();
    if (optional.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK).body(optional.get());
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }

  @Operation(summary = "Get an example response", description = "This is description of getting an example response")
  @GetMapping
  public ResponseEntity<?> list(@Parameter(required = true) @Valid @RequestBody ApiRequest<ExamplePageQueryRequest> request) {
    final ExamplePageQueryRequest examplePageQueryRequest = request.getData();
    final List<ExampleResponse> list = exampleResponseList.stream()
        .filter(exampleResponse -> exampleResponse.getStringField().contains(examplePageQueryRequest.getStringField()))
        .skip(examplePageQueryRequest.getCurrent() * examplePageQueryRequest.getSize())
        .limit(examplePageQueryRequest.getSize()).toList();
    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(list));
  }

  @Operation(summary = "Create a new example response", description = "This is description of creating a new example response")
  @PostMapping
  public ResponseEntity<?> create(@Parameter(required = true) @Valid @RequestBody ApiRequest<ExampleRequest> request) {
    final ExampleResponse exampleResponse = new ExampleResponse();
    BeanUtils.copyProperties(request.getData(), exampleResponse);
    exampleResponse.setBigDecimalField(IdGenerator.nextId());
    exampleResponseList.add(exampleResponse);
    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(exampleResponse));
  }

  @Operation(summary = "Update an existing example response", description = "This is description of update an existing example "
      + "response")
  @PutMapping
  public ResponseEntity<?> update(@Parameter(required = true) @Valid @RequestBody ApiRequest<ExampleRequest> request) {
    final ExampleRequest exampleRequest = request.getData();
    final Optional<ExampleResponse> optional = exampleResponseList.stream()
        .filter(exampleResponse -> exampleResponse.getBigDecimalField().equals(exampleRequest.getBigDecimalField())).findFirst();
    if (optional.isPresent()) {
      final ExampleResponse exampleResponse = optional.get();
      exampleResponse.setBooleanField(exampleRequest.getBooleanField());
      exampleResponse.setStringField(exampleRequest.getStringField());
      exampleResponse.setLocalDateTimeField(exampleRequest.getLocalDateTimeField());
      return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(exampleResponse));
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }

  @Operation(summary = "PartialUpdate an existing example response", description =
      "This is description of PartialUpdating an existing example "
          + "response")
  @PatchMapping
  public ResponseEntity<?> partialUpdate(@Parameter(required = true) @Valid @RequestBody ApiRequest<ExampleRequest> request) {
    final ExampleRequest exampleRequest = request.getData();
    final Optional<ExampleResponse> optional = exampleResponseList.stream()
        .filter(exampleResponse -> exampleResponse.getBigDecimalField().equals(exampleRequest.getBigDecimalField())).findFirst();
    if (optional.isPresent()) {
      final ExampleResponse exampleResponse = optional.get();
      exampleResponse.setStringField(exampleRequest.getStringField());
      return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(exampleResponse));
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }

  @Operation(summary = "Delete an existing example response", description = "This is description of deleting existing an example "
      + "response")
  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable BigDecimal id) {
    exampleResponseList.removeIf(exampleResponse -> exampleResponse.getBigDecimalField().equals(id));
    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
  }

}
