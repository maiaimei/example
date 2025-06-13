package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan(
//    basePackages = "org.example.**",
//    excludeFilters = @ComponentScan.Filter(
//        type = FilterType.REGEX,
//        pattern = "org.example.*.productcenter.*"
//    )
//)
public class MybatisApplication {

  public static void main(String[] args) {
    SpringApplication.run(MybatisApplication.class, args);
  }
}
