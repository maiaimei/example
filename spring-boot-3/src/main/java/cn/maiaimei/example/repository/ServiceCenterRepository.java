package cn.maiaimei.example.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Repository
public class ServiceCenterRepository {

  @GetMapping("/register")
  public String register() {
    log.info("register service");
    return "success";
  }

  @GetMapping("/stop")
  public String stop() {
    log.info("stop service");
    return "success";
  }
}
