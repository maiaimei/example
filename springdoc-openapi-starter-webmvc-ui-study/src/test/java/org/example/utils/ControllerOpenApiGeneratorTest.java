package org.example.utils;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
public class ControllerOpenApiGeneratorTest {

  public static void main(String[] args) {
    final OpenAPI openApi = new OpenAPI()
        .info(new Info()
            .title("API Documentation")
            .version("1.0.0")
            .description("Automatically generated API Documentation"));

    ControllerOpenApiGenerator generator = new ControllerOpenApiGenerator();

    // 扫描所有控制器
    ClassPathScanningCandidateComponentProvider provider =
        new ClassPathScanningCandidateComponentProvider(true);
    provider.addIncludeFilter(new AnnotationTypeFilter(RestController.class));

    String basePackage = "org.example.controller"; // 设置你的基础包
    for (BeanDefinition beanDefinition : provider.findCandidateComponents(basePackage)) {
      try {
        Class<?> controllerClass = Class.forName(beanDefinition.getBeanClassName());
        Paths paths = generator.generatePaths(controllerClass);
        paths.forEach((path, pathItem) -> {
          if (openApi.getPaths() == null) {
            openApi.setPaths(new Paths());
          }
          openApi.getPaths().addPathItem(path, pathItem);
        });
      } catch (ClassNotFoundException e) {
        log.error("Failed to load controller class", e);
      }
    }

    OpenApiUtils.printOpenApi(openApi);
  }
}
