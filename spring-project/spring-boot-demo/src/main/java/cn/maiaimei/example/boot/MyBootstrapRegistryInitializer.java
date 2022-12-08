package cn.maiaimei.example.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.BootstrapRegistryInitializer;

@Slf4j
public class MyBootstrapRegistryInitializer implements BootstrapRegistryInitializer {
    @Override
    public void initialize(BootstrapRegistry registry) {
        log.info("BootstrapRegistryInitializer.initialize");
    }
}
