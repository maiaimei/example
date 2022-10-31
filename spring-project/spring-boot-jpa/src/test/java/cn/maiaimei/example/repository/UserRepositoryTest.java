package cn.maiaimei.example.repository;

import cn.maiaimei.example.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void testFindAll() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            log.info("{}", user);
        }
    }
}
