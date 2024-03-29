package cn.maiaimei.example.controller;

import cn.maiaimei.example.registrar.AdditionOperation;
import cn.maiaimei.example.registrar.DivisionOperation;
import cn.maiaimei.example.registrar.MultiplicationOperation;
import cn.maiaimei.example.registrar.SubtractionOperation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/operation")
public class OperationController {

  @Resource
  private AdditionOperation additionOperation;

  @Resource
  private SubtractionOperation subtractionOperation;

  @Resource
  private MultiplicationOperation multiplicationOperation;

  @Resource
  private DivisionOperation divisionOperation;

  @GetMapping("/addition")
  public int addition(@RequestParam int a, @RequestParam int b) {
    final int c = additionOperation.addition(a, b);
    log.info("{} + {} = {}", a, b, c);
    return c;
  }

  @GetMapping("/subtraction")
  public int subtraction(@RequestParam int a, @RequestParam int b) {
    final int c = subtractionOperation.subtraction(a, b);
    log.info("{} - {} = {}", a, b, c);
    return c;
  }

  @GetMapping("/multiplication")
  public int multiplication(@RequestParam int a, @RequestParam int b) {
    final int c = multiplicationOperation.multiplication(a, b);
    log.info("{} * {} = {}", a, b, c);
    return c;
  }

  @GetMapping("/division")
  public int division(@RequestParam int a, @RequestParam int b) {
    final int c = divisionOperation.division(a, b);
    log.info("{} / {} = {}", a, b, c);
    return c;
  }

}
