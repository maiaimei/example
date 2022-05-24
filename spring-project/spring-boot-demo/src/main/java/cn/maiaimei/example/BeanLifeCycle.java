package cn.maiaimei.example;

import cn.maiaimei.example.beans.MyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * https://www.cnblogs.com/javazhiyin/p/10905294.html
 */
@Slf4j
public class BeanLifeCycle implements
        BeanNameAware,
        BeanClassLoaderAware,
        BeanFactoryAware,
        InitializingBean,
        DisposableBean {
    public BeanLifeCycle() {
        log.info("===== BeanLifeCycle ===== Bean实例化");
    }

    private MyRepository myRepository;

    @Autowired
    public void setUserMapper(MyRepository myRepository) {
        log.info("===== BeanLifeCycle ===== Bean属性注入");
        this.myRepository = myRepository;
    }

    @Override
    public void setBeanName(String name) {
        log.info("===== BeanLifeCycle ===== BeanNameAware.setBeanName");
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        log.info("===== BeanLifeCycle ===== BeanClassLoaderAware.setBeanClassLoader");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        log.info("===== BeanLifeCycle ===== BeanFactoryAware.setBeanFactory");
    }

    @PostConstruct
    public void postConstruct() {
        log.info("===== BeanLifeCycle ===== @PostConstruct");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("===== BeanLifeCycle ===== InitializingBean.afterPropertiesSet");
    }

    public void initMethod() {
        log.info("===== BeanLifeCycle ===== @Bean(initMethod = 'initMethod')");
    }

    @PreDestroy
    public void preDestroy() {
        log.info("===== BeanLifeCycle ===== @PreDestroy");
    }

    @Override
    public void destroy() throws Exception {
        log.info("===== BeanLifeCycle ===== DisposableBean.destroy");
    }

    public void destroyMethod() {
        log.info("===== BeanLifeCycle ===== @Bean(destroyMethod = 'destroyMethod')");
    }


}
