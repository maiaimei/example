package cn.maiaimei.example.controller;

import cn.maiaimei.example.repository.ServiceCenterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/service-center")
public class ServiceCenterController {

  @Autowired
  private ServiceCenterRepository serviceCenterRepository;

  @GetMapping("/register")
  public String register() {
    return serviceCenterRepository.register();
  }

  @GetMapping("/stop")
  public String stop() {
    return serviceCenterRepository.stop();
  }

  @GetMapping("/heartbeat")
  public String heartbeat() {
    log.info("heartbeat");
    return "success";
  }
}
