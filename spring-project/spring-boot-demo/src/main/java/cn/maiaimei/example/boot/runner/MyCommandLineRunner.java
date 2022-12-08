package cn.maiaimei.example.boot.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyCommandLineRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(MyCommandLineRunner.class);

    @Override
    public void run(String... args) throws Exception {
        log.info("===== ApplicationRunner and CommandLineRunner ===== CommandLineRunner.run");
    }
}
