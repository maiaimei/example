package cn.maiaimei.example.single.repository;

import cn.maiaimei.example.single.entity.User;
import cn.maiaimei.example.util.PasswordHelper;
import cn.maiaimei.example.util.SFID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void testInsert() {
        LocalDateTime now = LocalDateTime.now();

        User admin = User.builder()
                .id(SFID.randomSFID())
                .nickname("admin")
                .username("admin")
                .password(PasswordHelper.getRandomPassword())
                .is_enabled(Boolean.TRUE)
                .is_deleted(Boolean.FALSE)
                .gmtCreate(now)
                .gmtModified(now)
                .build();
        userRepository.save(admin);

        User guest = User.builder()
                .id(SFID.randomSFID())
                .nickname("guest")
                .username("guest")
                .password(PasswordHelper.getRandomPassword())
                .is_enabled(Boolean.TRUE)
                .is_deleted(Boolean.FALSE)
                .gmtCreate(now)
                .gmtModified(now)
                .build();
        userRepository.save(guest);
    }

    @Test
    void testUpdate() {
        LocalDateTime now = LocalDateTime.now();
        User user = userRepository.findOne(Example.of(User.builder().username("admin").build())).orElseGet(User::new);
        user.setIs_enabled(Boolean.FALSE);
        user.setGmtModified(LocalDateTime.now());
        userRepository.save(user);
    }

    @Test
    void testDeleteById() {
        User user = userRepository.findOne(Example.of(User.builder().username("admin").build())).orElseGet(User::new);
        userRepository.deleteById(user.getId());
    }

    @Test
    void testDeleteAll() {
        userRepository.deleteAll();
    }

    @Test
    void testFindOne() {
        User user = userRepository.findOne(Example.of(User.builder().username("admin").build())).orElseGet(User::new);
        log.info("{}", user);
    }

    @Test
    void testFindAll() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            log.info("{}", user);
        }
    }

}
