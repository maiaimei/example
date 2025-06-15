package org.example.mybatis.type;

import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.example.mybatis.annotation.TableName;
import org.example.mybatis.annotation.TypeHandler;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * 自定义TypeHandler注册表
 */
@Slf4j
public class CustomTypeHandlerRegistry {

  private final Map<Class<?>, Map<String, Class<?>>> entityTypeHandlers = new HashMap<>();
  private final Set<Class<?>> typeHandlerClasses = new HashSet<>();

  public void scanAndRegisterTypeHandlers(String basePackage) {
    ClassPathScanningCandidateComponentProvider scanner =
        new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(new AnnotationTypeFilter(TableName.class));

    Set<BeanDefinition> components = scanner.findCandidateComponents(basePackage);
    for (BeanDefinition component : components) {
      try {
        Class<?> entityClass = ClassUtils.forName(component.getBeanClassName(),
            Thread.currentThread().getContextClassLoader());
        processEntityClass(entityClass);
      } catch (ClassNotFoundException e) {
        log.error("Failed to load class: " + component.getBeanClassName(), e);
      }
    }
  }

  private void processEntityClass(Class<?> entityClass) {
    Map<String, Class<?>> fieldTypeHandlers = new HashMap<>();

    ReflectionUtils.doWithFields(entityClass, field -> {
      TypeHandler annotation = field.getAnnotation(TypeHandler.class);
      if (annotation != null) {
        Class<?> handlerClass = annotation.value();
        fieldTypeHandlers.put(field.getName(), handlerClass);
        typeHandlerClasses.add(handlerClass);
      }
    });

    if (!fieldTypeHandlers.isEmpty()) {
      entityTypeHandlers.put(entityClass, fieldTypeHandlers);
    }
  }

  public Set<Class<?>> getTypeHandlers() {
    return Collections.unmodifiableSet(typeHandlerClasses);
  }

  public Class<?> getTypeHandler(Class<?> entityClass, String fieldName) {
    Map<String, Class<?>> fieldHandlers = entityTypeHandlers.get(entityClass);
    return fieldHandlers != null ? fieldHandlers.get(fieldName) : null;
  }
}