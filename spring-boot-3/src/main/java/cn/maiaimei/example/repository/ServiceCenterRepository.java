package cn.maiaimei.example.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ServiceCenterRepository {

  public String register() {
    log.info("register service");
    return "success";
  }
  
  public String stop() {
    log.info("stop service");
    return "success";
  }
}
