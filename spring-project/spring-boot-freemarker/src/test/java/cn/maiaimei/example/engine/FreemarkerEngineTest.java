package cn.maiaimei.example.engine;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.maiaimei.example.config.TestConfig;
import cn.maiaimei.example.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
class FreemarkerEngineTest {

    @Autowired
    FreemarkerEngine freemarkerEngine;

    final Snowflake snowflake = IdUtil.getSnowflake(1, 1);

    @Test
    void generateUser() {
        final User user = User.builder()
                .id(snowflake.nextId())
                .username(RandomStringUtils.randomAlphabetic(5))
                .password(RandomStringUtils.randomAlphanumeric(8))
                .gmtCreated(LocalDateTime.now())
                .gmtModified(LocalDateTime.now())
                .build();
        freemarkerEngine.writeToConsole("user.ftl", user);

        Map<String, Object> map = new HashMap<>();
        map.put("id", snowflake.nextId());
        map.put("username", RandomStringUtils.randomAlphabetic(5));
        map.put("password", RandomStringUtils.randomAlphanumeric(8));
        map.put("gmtCreated", LocalDateTime.now());
        map.put("gmtModified", LocalDateTime.now());
        freemarkerEngine.writeToConsole("user.ftl", map);
    }

    @Test
    void generateUserList() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final User user = User.builder()
                    .id(snowflake.nextId())
                    .username(RandomStringUtils.randomAlphabetic(5))
                    .password(RandomStringUtils.randomAlphanumeric(8))
                    .gmtCreated(LocalDateTime.now())
                    .gmtModified(LocalDateTime.now())
                    .build();
            users.add(user);
        }
        Map<String, Object> root = new HashMap<>();
        root.put("users", users);
        freemarkerEngine.writeToConsole("user-list.ftl", root);
    }

    @Test
    void testList() {
        Map<String, Object> map = new HashMap<>();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");

        List<User> users = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final User user = User.builder()
                    .id(snowflake.nextId())
                    .username(RandomStringUtils.randomAlphabetic(5))
                    .password(RandomStringUtils.randomAlphanumeric(8))
                    .gmtCreated(LocalDateTime.now())
                    .gmtModified(LocalDateTime.now())
                    .build();
            users.add(user);
        }

        Map<String, Object> root = new HashMap<>();
        root.put("num", 5);
        root.put("hash", map);
        root.put("users", users);
        freemarkerEngine.writeToConsole("list.ftl", root);
    }
}
