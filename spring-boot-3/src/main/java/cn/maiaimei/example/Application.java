package cn.maiaimei.example;

import cn.maiaimei.example.registrar.EnableOperation;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.annotation.WebServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * 在启动类上添加{@link ServletComponentScan}注解，该注解可以扫描{@link WebFilter}、{@link WebServlet}、{@link
 * WebListener}注解
 */
@EnableOperation
@ServletComponentScan
@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
