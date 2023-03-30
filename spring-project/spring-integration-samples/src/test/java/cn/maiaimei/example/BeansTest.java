package cn.maiaimei.example;

import cn.maiaimei.example.config.TestConfg;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfg.class)
class BeansTest {

    @Value("spring.profiles.active")
    private String activeProfile;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void test() {
        log.info("activeProfile is {}", activeProfile);
        log.info("myDataSourceProperties {} exist", applicationContext.containsBean("myDataSourceProperties") ? "is" : "is not");
        log.info("h2DataSource {} exist", applicationContext.containsBean("h2DataSource") ? "is" : "is not");
        log.info("mysqlDataSource {} exist", applicationContext.containsBean("mysqlDataSource") ? "is" : "is not");
    }

}
