package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
    basePackages = "org.example.**",
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "org.example.*.productcenter.*"
    )
)
public class MainApplication {

  public static void main(String[] args) {
    SpringApplication.run(MainApplication.class, args);
  }
}
