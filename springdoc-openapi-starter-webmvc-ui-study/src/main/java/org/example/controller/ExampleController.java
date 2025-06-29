package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.model.*;
import org.example.model.request.ExamplePageQueryRequest;
import org.example.model.request.ExampleRequest;
import org.example.model.response.ExampleResponse;
import org.example.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ExampleController", description = "This is description of BasicExampleController")
@RestController
@RequestMapping("/examples")
public class ExampleController {

  private static final List<ExampleResponse> exampleResponseList = new ArrayList<>();

  @Operation(summary = "Get an example response", description = "This is description of getting an example response")
  @GetMapping("/{id}")
  public ExampleResponse get(@PathVariable BigDecimal id) {
    final Optional<ExampleResponse> optional = exampleResponseList.stream()
        .filter(exampleResponse -> exampleResponse.getBigDecimalField().equals(id)).findFirst();
    return optional.get();
  }

  @Operation(summary = "List example responses", description = "This is description of listing responses")
  @GetMapping("list")
  public List<ExampleResponse> list(
      @Parameter(required = true) @Valid @RequestBody ApiRequest<ExamplePageQueryRequest> request) {
    final ExamplePageQueryRequest examplePageQueryRequest = request.getData();
    final List<ExampleResponse> list = exampleResponseList.stream()
        .filter(exampleResponse -> exampleResponse.getStringField().contains(examplePageQueryRequest.getStringField()))
        .toList();
    return list;
  }

  @Operation(summary = "Page list example responses", description = "This is description of paging list responses")
  @GetMapping("page-list")
  public PageableSearchResult<ExampleResponse> pageList(
      @Parameter(required = true) @Valid @RequestBody ApiRequest<PageableSearchRequest<ExamplePageQueryRequest>> request) {
    final PageableSearchRequest<ExamplePageQueryRequest> pageableSearchRequest = request.getData();
    final ExamplePageQueryRequest examplePageQueryRequest = pageableSearchRequest.getFilter();
    final PageCriteria page = pageableSearchRequest.getPage();
    final List<ExampleResponse> list = exampleResponseList.stream()
        .filter(exampleResponse -> exampleResponse.getStringField().contains(examplePageQueryRequest.getStringField()))
        .skip((long) page.getCurrent() * page.getSize())
        .limit(page.getSize()).toList();
    PageableSearchResult<ExampleResponse> result = new PageableSearchResult<>();
    result.setTotalRecords((long) exampleResponseList.size());
    result.setTotalPages((long) Math.ceil((double) result.getTotalRecords() / page.getSize()));
    result.setCurrentPageNumber(page.getCurrent());
    result.setPageSize(page.getSize());
    result.setRecords(list);
    return result;
  }

  @Operation(summary = "Create a new example response", description = "This is description of creating a new example response")
  @PostMapping
  public ExampleResponse create(
      @Parameter(required = true) @Valid @RequestBody ApiRequest<ExampleRequest> request) {
    final ExampleResponse exampleResponse = new ExampleResponse();
    BeanUtils.copyProperties(request.getData(), exampleResponse);
    exampleResponse.setBigDecimalField(IdGenerator.nextId());
    exampleResponseList.add(exampleResponse);
    return exampleResponse;
  }

  @Operation(summary = "Update an existing example response", description = "This is description of update an existing example "
      + "response")
  @PutMapping
  public ExampleResponse update(
      @Parameter(required = true) @Valid @RequestBody ApiRequest<ExampleRequest> request) {
    final ExampleRequest exampleRequest = request.getData();
    final Optional<ExampleResponse> optional = exampleResponseList.stream()
        .filter(exampleResponse -> exampleResponse.getBigDecimalField().equals(exampleRequest.getBigDecimalField())).findFirst();
    if (optional.isPresent()) {
      final ExampleResponse exampleResponse = optional.get();
      exampleResponse.setBooleanField(exampleRequest.getBooleanField());
      exampleResponse.setStringField(exampleRequest.getStringField());
      exampleResponse.setLocalDateTimeField(exampleRequest.getLocalDateTimeField());
      return exampleResponse;
    }
    return null;
  }

  @Operation(summary = "PartialUpdate an existing example response", description =
      "This is description of PartialUpdating an existing example "
          + "response")
  @PatchMapping
  public void partialUpdate(
      @Parameter(required = true) @Valid @RequestBody ApiRequest<ExampleRequest> request) {
    final ExampleRequest exampleRequest = request.getData();
    final Optional<ExampleResponse> optional = exampleResponseList.stream()
        .filter(exampleResponse -> exampleResponse.getBigDecimalField().equals(exampleRequest.getBigDecimalField())).findFirst();
    if (optional.isPresent()) {
      final ExampleResponse exampleResponse = optional.get();
      exampleResponse.setStringField(exampleRequest.getStringField());
    }
  }

  @Operation(summary = "Delete an existing example response", description = "This is description of deleting existing an example "
      + "response")
  @DeleteMapping("/{id}")
  public void delete(@PathVariable BigDecimal id) {
    exampleResponseList.removeIf(exampleResponse -> exampleResponse.getBigDecimalField().equals(id));
  }

}
