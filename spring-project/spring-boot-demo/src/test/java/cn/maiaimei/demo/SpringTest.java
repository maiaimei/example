package cn.maiaimei.demo;

import cn.maiaimei.demo.converter.StringToFemaleConditionalConverter;
import cn.maiaimei.demo.converter.StringToMaleGenericConverter;
import cn.maiaimei.demo.converter.StringToPeopleConverter;
import cn.maiaimei.demo.converter.TeacherToBookConverterFactory;
import cn.maiaimei.demo.model.*;
import cn.maiaimei.demo.service.MathService;
import cn.maiaimei.demo.tx.service.UserService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.event.DefaultEventListenerFactory;
import org.springframework.context.event.EventListenerMethodProcessor;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.DefaultConversionService;

public class SpringTest {
    private static final Logger log = LoggerFactory.getLogger(SpringTest.class);

    @Test
    void testConverterFactory() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfConverter.class);
        TeacherToBookConverterFactory factory = applicationContext.getBean(TeacherToBookConverterFactory.class);
        System.out.println(factory.getConverter(EnglishBook.class).convert(EnglishTeacher.builder().name("Kate").build()));
        System.out.println(factory.getConverter(MathBook.class).convert(MathTeacher.builder().name("Kate").build()));
        applicationContext.close();
    }

    @Test
    void testConversionService() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfConverter.class);
        DefaultConversionService conversionService = applicationContext.getBean(DefaultConversionService.class);
        System.out.println(conversionService.convert("2:Koi", Boy.class));
        System.out.println(conversionService.convert("4:Ivy", getTypeDescriptor(Girl.class)));
        System.out.println(conversionService.convert("5:Amy", getTypeDescriptor(String.class), getTypeDescriptor(Woman.class)));
        applicationContext.close();
    }

    @Test
    void testConverter() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfConverter.class);
        StringToPeopleConverter stringToPeopleConverter = applicationContext.getBean(StringToPeopleConverter.class);
        StringToMaleGenericConverter stringToMaleGenericConverter = applicationContext.getBean(StringToMaleGenericConverter.class);
        StringToFemaleConditionalConverter stringToFemaleConditionalConverter = applicationContext.getBean(StringToFemaleConditionalConverter.class);
        System.out.println(stringToPeopleConverter.convert("1:Leo"));
        System.out.println(stringToMaleGenericConverter.convert("2:Koi", getTypeDescriptor(String.class), getTypeDescriptor(Boy.class)));
        System.out.println(stringToMaleGenericConverter.convert("3:Tom", getTypeDescriptor(String.class), getTypeDescriptor(Man.class)));
        System.out.println(stringToFemaleConditionalConverter.convert("4:Ivy", getTypeDescriptor(String.class), getTypeDescriptor(Girl.class)));
        System.out.println(stringToFemaleConditionalConverter.convert("5:Amy", getTypeDescriptor(String.class), getTypeDescriptor(Woman.class)));
        applicationContext.close();
    }

    private TypeDescriptor getTypeDescriptor(Class<?> clazz) {
        return new TypeDescriptor(ResolvableType.forRawClass(clazz), null, null);
    }

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
     * beanDefinition -> {@link DefaultListableBeanFactory#registerBeanDefinition(java.lang.String, org.springframework.beans.factory.config.BeanDefinition)}
     * org.springframework.context.annotation.internalConfigurationAnnotationProcessor = {@link ConfigurationClassPostProcessor}
     * org.springframework.context.annotation.internalAutowiredAnnotationProcessor = {@link AutowiredAnnotationBeanPostProcessor}
     * org.springframework.context.annotation.internalCommonAnnotationProcessor = {@link CommonAnnotationBeanPostProcessor}
     * org.springframework.context.event.internalEventListenerProcessor = {@link EventListenerMethodProcessor}
     * org.springframework.context.event.internalEventListenerFactory = {@link DefaultEventListenerFactory}
     * <p>
     * beanPostProcessor
     * ApplicationContextAwareProcessor implements BeanPostProcessor -> setXxxAware
     * <p>
     */
    @Test
    void testBeanRegisterAndDependencyInjection() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        printBeanNames(applicationContext, beanDefinitionNames);
        // 获取Pink对象
        log.info("Pink对象={}", applicationContext.getBean("pinkFactoryBean"));
        // 获取Pink对象的代理对象
        log.info("Pink对象的代理对象={}", applicationContext.getBean("&pinkFactoryBean"));
        applicationContext.close();
    }

    void printBeanNames(ApplicationContext applicationContext, String[] beanDefinitionNames) {
        for (String beanDefinitionName : beanDefinitionNames) {
            log.info("{}={}", beanDefinitionName, applicationContext.getBean(beanDefinitionName));
        }
    }
}
