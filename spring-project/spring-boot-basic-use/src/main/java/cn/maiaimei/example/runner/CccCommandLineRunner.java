package cn.maiaimei.example.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

@Slf4j
@Order(1)
//@Component
public class CccCommandLineRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        log.info("{}", args);
    }
}
