package cn.maiaimei.example.engine;

import cn.maiaimei.example.config.TestConfig;
import cn.maiaimei.example.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
class FreemarkerEngineTest {

    @Autowired
    FreemarkerEngine freemarkerEngine;

    @Test
    void generateRequest01() {
        final User user = User.builder()
                .id(184309536616640513L)
                .username("admin")
                .password("123456")
                .gmtCreated(LocalDateTime.now())
                .gmtModified(LocalDateTime.now())
                .build();
        final String request = freemarkerEngine.process("user-request.ftl", user);
        assertTrue(request.contains("\"id\": 184309536616640513"));
        log.info("{}", request);
    }

    @Test
    void generateRequest02() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 184309536616640513L);
        map.put("username", "admin");
        map.put("password", "123456");
        map.put("gmtCreated", LocalDateTime.now());
        map.put("gmtModified", LocalDateTime.now());
        final String request = freemarkerEngine.process("user-request.ftl", map);
        assertTrue(request.contains("\"id\": 184309536616640513"));
        log.info("{}", request);
    }
}
