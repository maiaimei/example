# Bean Register

1. 使用@Component、@Controller、@RestController、@Service、@Repository、@Configuration注解
   - 适用于自己编写的类
   - 需要组件扫描
   - 最简单直接的方式
2. 使用@Configuration和@Bean注解
   - 适用于注册第三方库的类
   - 需要程序化创建bean
   - 需要在注册时进行配置
3. 使用BeanFactoryPostProcessor注册
   - 需要在容器初始化阶段干预bean注册
   - 需要修改已存在的bean定义
4. 使用BeanDefinitionRegistryPostProcessor注册
   - 需要在容器初始化阶段干预bean注册
   - 需要修改已存在的bean定义
5. 使用@Import注解导入类
6. 使用@Import注解导入实现ImportBeanDefinitionRegistrar接口的类
   - 适用于框架开发
   - 需要基于条件注册多个相关的bean
7. 使用@Import注解导入实现ImportSelector接口的类
   * 基于配置属性动态导入不同的类实现
8. 使用FactoryBean接口
   - 需要自定义bean的创建逻辑
   - bean的创建过程比较复杂

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

