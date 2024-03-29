package cn.maiaimei.example.registrar;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class OperationImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar,
    EnvironmentAware, ResourceLoaderAware {

  private Environment environment;

  private ResourceLoader resourceLoader;

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  @Override
  public void setResourceLoader(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  @Override
  public void registerBeanDefinitions(AnnotationMetadata metadata,
      BeanDefinitionRegistry registry) {
    // 获取扫描路径
    final Set<String> basePackages = getBasePackages(metadata);
    // 获取扫描路径下的 @Operation 注解
    final ClassPathScanningCandidateComponentProvider scanner = getScanner();
    for (String basePackage : basePackages) {
      final Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
      for (BeanDefinition candidateComponent : candidateComponents) {
        if (candidateComponent instanceof AnnotatedBeanDefinition beanDefinition) {
          // 生成代理并注册
          registerOperationComponent(beanDefinition.getMetadata(), registry);
        }
      }
    }
  }

  private Set<String> getBasePackages(AnnotationMetadata metadata) {
    final Map<String, Object> attributes = metadata.getAnnotationAttributes(
        EnableOperation.class.getName());
    Assert.notNull(attributes, "EnableOperation annotation not found");
    Set<String> basePackages = new HashSet<>();
    for (String basePackage : (String[]) attributes.get("value")) {
      if (StringUtils.hasText(basePackage)) {
        basePackages.add(basePackage);
      }
    }
    if (CollectionUtils.isEmpty(basePackages)) {
      basePackages.add(ClassUtils.getPackageName(metadata.getClassName()));
    }
    return basePackages;
  }

  private ClassPathScanningCandidateComponentProvider getScanner() {
    final ClassPathScanningCandidateComponentProvider scanner =
        new ClassPathScanningCandidateComponentProvider() {
          @Override
          protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
            return Boolean.TRUE;
          }
        };
    scanner.setEnvironment(environment);
    scanner.setResourceLoader(resourceLoader);
    scanner.addIncludeFilter(new AnnotationTypeFilter(Operation.class));
    return scanner;
  }

  private void registerOperationComponent(AnnotationMetadata metadata,
      BeanDefinitionRegistry registry) {
    final String className = metadata.getClassName();
    final BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(
        OperationFactoryBean.class);
    beanDefinitionBuilder.addPropertyValue("type", className);
    beanDefinitionBuilder.addPropertyValue("expression",
        metadata.getAnnotationAttributes(Operation.class.getName()).get("value")
    );
    beanDefinitionBuilder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
    final AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
    try {
      beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE,
          Class.forName(className));
    } catch (ClassNotFoundException e) {
      throw new BeanInstantiationException(this.getClass(), className + " not found", e);
    }
    final BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinition,
        className);
    BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, registry);
  }
}
