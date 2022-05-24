package cn.maiaimei.example;

import cn.maiaimei.framework.spring.boot.web.EnableGlobalException;
import cn.maiaimei.framework.spring.boot.web.EnableGlobalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @ServletComponentScan 用于扫描Servlet组件，如： @WebListener @WebFilter
 */
@Slf4j
@EnableGlobalException(isShowTrace = false)
@EnableGlobalResponse
//@ServletComponentScan
@SpringBootApplication
public class SpringBootDemoApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringBootDemoApplication.class, args);

//        System.out.println("==================================================");
//
//        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
//        for (String beanDefinitionName : beanDefinitionNames) {
//            Object bean = applicationContext.getBean(beanDefinitionName);
//            if (bean.getClass().getName().contains("cn.maiaimei")) {
//                log.info("{} => {}", beanDefinitionName, bean);
//            }
//        }
//
//        System.out.println("==================================================");
//
//        BeanDependencyInjection beanDependencyInjection = applicationContext.getBean(BeanDependencyInjection.class);
//        beanDependencyInjection.showMember();
    }
}