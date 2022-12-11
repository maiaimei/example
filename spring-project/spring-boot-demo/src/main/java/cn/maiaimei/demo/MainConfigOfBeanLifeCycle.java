package cn.maiaimei.demo;

import cn.maiaimei.demo.beans.XxxBean;
import cn.maiaimei.demo.beans.YyyBean;
import cn.maiaimei.demo.processor.XxxInstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link AbstractAutowireCapableBeanFactory#doCreateBean(java.lang.String, org.springframework.beans.factory.support.RootBeanDefinition, java.lang.Object[])}
 * {@link DisposableBeanAdapter#destroy()}
 */
@Configuration
public class MainConfigOfBeanLifeCycle {
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
