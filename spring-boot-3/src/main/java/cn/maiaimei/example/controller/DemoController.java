package cn.maiaimei.example.controller;

import cn.maiaimei.example.annotation.CustomAnnotation;
import cn.maiaimei.example.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DemoController {

  @Autowired
  private DemoService demoService;

  @CustomAnnotation
  @RequestMapping(value = "/demo/{num}", method = RequestMethod.GET)
  public String demo(@PathVariable Integer num) {
    log.info("执行目标方法");
    return demoService.demo(num);
  }
}
