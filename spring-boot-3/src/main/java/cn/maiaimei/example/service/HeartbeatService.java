package cn.maiaimei.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HeartbeatService {

  public String heartbeat() {
    log.info("heartbeat");
    return "success";
  }
}
