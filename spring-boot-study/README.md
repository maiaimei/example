# PostProcessor

Spring Boot中的后置处理器(PostProcessor)主要分为两大类：BeanFactoryPostProcessor和BeanPostProcessor。

## BeanFactoryPostProcessor

```java
@FunctionalInterface
public interface BeanFactoryPostProcessor {
	void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}

public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {
	void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException;

	@Override
	default void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
	}
}
```

### PropertySourcesPlaceholderConfigurer

- 解析${...}表达式
- 处理配置文件中的占位符
- 加载外部配置文件

### ConfigurationClassPostProcessor

- 解析@ComponentScan
- 处理@Configuration注解
- 处理@Bean方法
- 处理@Import注解

## BeanPostProcessor

### AutowiredAnnotationBeanPostProcessor

- 处理@Autowired注解
- 处理@Value注解
- 自动注入依赖

### CommonAnnotationBeanPostProcessor

- 处理@PostConstruct注解
- 处理@PreDestroy注解
- 处理@Resource注解

### AsyncAnnotationBeanPostProcessor

- 处理@Async注解
- 实现方法异步执行

### ScheduledAnnotationBeanPostProcessor

- 处理@Scheduled注解
- 管理定时任务

### AopInfrastructureBean

- 实现AOP功能
- 处理@Aspect注解
- 创建代理对象

