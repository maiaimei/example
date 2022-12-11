package cn.maiaimei.demo.processor;

import cn.maiaimei.demo.color.Gray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;

public class XxxBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    private static final Logger log = LoggerFactory.getLogger(XxxBeanDefinitionRegistryPostProcessor.class);

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        log.info("All regular bean definitions will have been loaded, but no beans will have been instantiated yet. This allows for adding further bean definitions before the next post-processing phase kicks in.");
        log.info("bean count: {}", registry.getBeanDefinitionCount());
        log.info("registerBeanDefinition gray start");
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(Gray.class);
        beanDefinition.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
        registry.registerBeanDefinition("gray", beanDefinition);
        log.info("registerBeanDefinition gray end");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.info("bean count: {}", beanFactory.getBeanDefinitionCount());
    }
}
