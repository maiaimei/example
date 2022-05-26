package cn.maiaimei.example;

import cn.maiaimei.framework.spring.boot.web.EnableGlobalException;
import cn.maiaimei.framework.spring.boot.web.EnableGlobalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

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
        log.info("the raw String[] arguments: {}", String.join(",", args));

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

    @Autowired
    private ApplicationArguments applicationArguments;

    @PostConstruct
    public void printArguments() {
        // the raw String[] arguments
        String[] sourceArgs = applicationArguments.getSourceArgs();
        log.info("the raw String[] arguments: {}", String.join(",", sourceArgs));

        // non-option arguments
        List<String> nonOptionArgs = applicationArguments.getNonOptionArgs();
        log.info("non-option arguments: {}", nonOptionArgs);

        // option arguments, e.g. --server.port=9999
        Set<String> optionNames = applicationArguments.getOptionNames();
        for (String optionName : optionNames) {
            List<String> optionValues = applicationArguments.getOptionValues(optionName);
            log.info("option arguments, name={}, value={}", optionName, optionValues);
        }

        // system arguments
        log.info("system arguments, author={}", System.getProperty("author"));
    }
}