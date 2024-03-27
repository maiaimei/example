package cn.maiaimei.example;

import cn.maiaimei.example.registrar.EnableOperation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableOperation
@SpringBootApplication(scanBasePackages = {"cn.maiaimei.example"})
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
