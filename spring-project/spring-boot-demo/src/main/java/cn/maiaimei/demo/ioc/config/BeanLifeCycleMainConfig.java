package cn.maiaimei.demo.ioc.config;

import cn.maiaimei.demo.ioc.component.XxxBean;
import cn.maiaimei.demo.ioc.component.XxxInstantiationAwareBeanPostProcessor;
import cn.maiaimei.demo.ioc.component.YyyBean;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link AbstractAutowireCapableBeanFactory#doCreateBean(java.lang.String, org.springframework.beans.factory.support.RootBeanDefinition, java.lang.Object[])}
 * {@link DisposableBeanAdapter#destroy()}
 */
@Configuration
public class BeanLifeCycleMainConfig {
    @Bean
    public XxxInstantiationAwareBeanPostProcessor xxxInstantiationAwareBeanPostProcessor() {
        return new XxxInstantiationAwareBeanPostProcessor();
    }

    @Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
    public XxxBean xxxBean() {
        return new XxxBean();
    }

    @Bean
    public YyyBean yyyBean() {
        return new YyyBean();
    }
}
