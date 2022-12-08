package cn.maiaimei.demo;

import cn.maiaimei.demo.aop.AopMainConfig;
import cn.maiaimei.demo.aop.MathService;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AopTest {
    @Test
    void testAop() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AopMainConfig.class);
        MathService mathService = applicationContext.getBean(MathService.class);
        mathService.div(1, 0);
        applicationContext.close();
    }
}
