package cn.maiaimei.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DemoService {

  public String demo(Integer num) {
    log.info("执行目标方法");
    return String.valueOf(1 / num);
  }
}
