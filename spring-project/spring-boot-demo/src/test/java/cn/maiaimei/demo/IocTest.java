package cn.maiaimei.demo;

import cn.maiaimei.demo.ioc.config.BeanLifeCycleMainConfig;
import cn.maiaimei.demo.ioc.config.BeanRegisterAndDependencyInjectionMainConfig;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class IocTest {
    private static final Logger log = LoggerFactory.getLogger(IocTest.class);

    @Test
    void testBeanLifeCycle() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanLifeCycleMainConfig.class);
        applicationContext.close();
    }

    @Test
    void testBeanRegisterAndDependencyInjection() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanRegisterAndDependencyInjectionMainConfig.class);
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
