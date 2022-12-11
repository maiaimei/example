package cn.maiaimei.demo;

import cn.maiaimei.demo.service.MathService;
import cn.maiaimei.demo.tx.service.UserService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.event.DefaultEventListenerFactory;
import org.springframework.context.event.EventListenerMethodProcessor;

public class SpringTest {
    private static final Logger log = LoggerFactory.getLogger(SpringTest.class);

    @Test
    void testEvent() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfApplicationListener.class);
        // 发布事件
        applicationContext.publishEvent(new ApplicationEvent("Hello, event") {
        });
        applicationContext.close();
    }

    @Test
    void testTx() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfTx.class);
        UserService userService = applicationContext.getBean(UserService.class);
        userService.insert();
        applicationContext.close();
    }

    @Test
    void testAop() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfAop.class);
        MathService mathService = applicationContext.getBean(MathService.class);
        /**
         * {@link CglibAopProxy.DynamicAdvisedInterceptor#intercept(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], org.springframework.cglib.proxy.MethodProxy)}
         */
        mathService.div(1, 1);
        applicationContext.close();
    }

    @Test
    void testBeanFactoryPostProcessor() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfBeanFactoryPostProcessor.class);
        applicationContext.close();
    }

    @Test
    void testBeanLifeCycle() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfBeanLifeCycle.class);
        applicationContext.close();
    }

    /**
     * {@link DefaultListableBeanFactory} beanFactory
     * <p>
     * beanDefinitionMap:
     * org.springframework.context.annotation.internalConfigurationAnnotationProcessor = {@link ConfigurationClassPostProcessor}
     * org.springframework.context.annotation.internalAutowiredAnnotationProcessor = {@link AutowiredAnnotationBeanPostProcessor}
     * org.springframework.context.annotation.internalCommonAnnotationProcessor = {@link CommonAnnotationBeanPostProcessor}
     * org.springframework.context.event.internalEventListenerProcessor = {@link EventListenerMethodProcessor}
     * org.springframework.context.event.internalEventListenerFactory = {@link DefaultEventListenerFactory}
     * <p>
     * beanPostProcessor
     * ApplicationContextAwareProcessor implements BeanPostProcessor -> setXxxAware
     * ApplicationListenerDetector implements DestructionAwareBeanPostProcessor, MergedBeanDefinitionPostProcessor
     * <p>
     * TODO: 调试
     * refresh() -> invokeBeanFactoryPostProcessors(beanFactory) -> {@link ConfigurationClassPostProcessor#postProcessBeanDefinitionRegistry(org.springframework.beans.factory.support.BeanDefinitionRegistry)}
     */
    @Test
    void testBeanRegisterAndDependencyInjection() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            log.info("{}={}", beanDefinitionName, applicationContext.getBean(beanDefinitionName));
        }
        // 获取Pink对象
        log.info("Pink对象={}", applicationContext.getBean("pinkFactoryBean"));
        // 获取Pink对象的代理对象
        log.info("Pink对象的代理对象={}", applicationContext.getBean("&pinkFactoryBean"));
        applicationContext.close();
    }
}
