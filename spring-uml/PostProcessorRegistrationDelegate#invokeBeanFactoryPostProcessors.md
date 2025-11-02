PostProcessorRegistrationDelegate#invokeBeanFactoryPostProcessors方法是Spring容器初始化的核心环节，负责执行所有BeanFactoryPostProcessor后置处理器。该方法在AbstractApplicationContext#refresh()方法中被调用，位于Bean实例化之前的关键阶段。

**方法执行流程**

1. **处理BeanDefinitionRegistryPostProcessor实现‌**
   * 首先处理实现了PriorityOrdered接口的BeanDefinitionRegistryPostProcessor，按优先级排序后依次执行、
   * 接着处理实现了Ordered接口的BeanDefinitionRegistryPostProcessor，同样按顺序执行
   * 最后处理剩余的BeanDefinitionRegistryPostProcessor实现。

2. **处理BeanFactoryPostProcessor实现‌**
   * 按照PriorityOrdered、Ordered、无顺序分类的顺序执行所有BeanFactoryPostProcessor
   * BeanDefinitionRegistryPostProcessor的执行优先级高于普通BeanFactoryPostProcessor

3. **特殊处理逻辑‌**
   * 方法内部通过beanFactory.getBean()对后置处理器进行实例化
   * 支持通过ConfigurableApplicationContext#addBeanFactoryPostProcessorAPI手动添加的处理器
   * 在单例Bean实例化前完成所有注册的BeanFactoryPostProcessor的调用

**核心功能特性**

1. **执行时机控制‌**：在BeanDefinition加载完成后、任何Bean实例化之前执行，确保对配置元数据的修改能够生效。
2. **排序机制支持‌**：全面支持PriorityOrdered和Ordered接口，允许开发者精确控制处理器的执行顺序。
3. **类型区分处理‌**：明确区分BeanDefinitionRegistryPostProcessor和BeanFactoryPostProcessor，前者专门用于动态注册BeanDefinition，后者用于修改已存在的BeanDefinition。

**实际应用场景**

该方法为Spring容器提供了强大的扩展能力，使得开发者能够在Bean实例化前对配置进行干预。常见的应用包括属性占位符处理、配置类解析、AOP基础设施准备等。通过这种机制，Spring实现了在不修改核心代码的情况下，允许外部扩展对容器行为进行定制。
