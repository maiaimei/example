package org.example.controller;

import java.math.BigDecimal;
import java.util.List;
import org.example.model.domain.JsonbTest;
import org.example.service.JsonbTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jsonb")
public class JsonbTestController {

  @Autowired
  private JsonbTestService jsonbTestService;

  @GetMapping
  public List<JsonbTest> getJsonbTestList() {
    return jsonbTestService.getJsonbTestList();
  }

  @GetMapping("/{id}")
  public JsonbTest getJsonbTest(@PathVariable BigDecimal id) {
    return jsonbTestService.getJsonbTest(id);
  }

  @PostMapping
  public JsonbTest createJsonbTest(@RequestBody JsonbTest jsonbTest) {
    return jsonbTestService.createJsonbTest(jsonbTest);
  }

  @PutMapping
  public JsonbTest updateJsonbTest(@RequestBody JsonbTest jsonbTest) {
    return jsonbTestService.updateJsonbTest(jsonbTest);
  }

  @DeleteMapping("/{id}")
  public void deleteJsonbTest(@PathVariable BigDecimal id) {
    jsonbTestService.deleteJsonbTest(id);
  }
}
