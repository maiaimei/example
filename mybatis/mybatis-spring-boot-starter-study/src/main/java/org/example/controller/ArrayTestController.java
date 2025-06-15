package org.example.controller;

import java.math.BigDecimal;
import java.util.List;
import org.example.model.domain.ArrayTest;
import org.example.service.ArrayTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/array")
public class ArrayTestController {

  @Autowired
  private ArrayTestService arrayTestService;

  @GetMapping
  public List<ArrayTest> getArrayTestList() {
    return arrayTestService.getArrayTestList();
  }

  @GetMapping("/{id}")
  public ArrayTest getArrayTest(@PathVariable BigDecimal id) {
    return arrayTestService.getArrayTest(id);
  }

  @PostMapping
  public ArrayTest createArrayTest(@RequestBody ArrayTest arrayTest) {
    return arrayTestService.createArrayTest(arrayTest);
  }

  @PutMapping
  public ArrayTest updateArrayTest(@RequestBody ArrayTest arrayTest) {
    return arrayTestService.updateArrayTest(arrayTest);
  }

  @DeleteMapping("/{id}")
  public void deleteArrayTest(@PathVariable BigDecimal id) {
    arrayTestService.deleteArrayTest(id);
  }
}
