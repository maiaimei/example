package cn.maiaimei.example;

import cn.maiaimei.example.util.SFID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class SFIDTest {

  @Test
  public void test() {
    for (int i = 0; i < 3; i++) {
      log.info("{}", SFID.nextId());
    }
  }
}
