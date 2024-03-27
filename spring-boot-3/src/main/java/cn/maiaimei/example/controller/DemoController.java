package cn.maiaimei.example.controller;

import cn.maiaimei.example.annotation.CustomAnnotation;
import cn.maiaimei.example.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

  @Autowired
  private DemoService demoService;

  @CustomAnnotation
  @RequestMapping(value = "/demo/{num}", method = RequestMethod.GET)
  public String demo(@PathVariable Integer num) {
    return demoService.demo(num);
  }
}
