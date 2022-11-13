package cn.maiaimei.example.multiple.jpa.ds1.repository;

import cn.maiaimei.example.multiple.jpa.ds1.entity.FirstUser;
import cn.maiaimei.example.util.PasswordHelper;
import cn.maiaimei.example.util.SFID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import java.util.List;

@Slf4j
@SpringBootTest
public class FirstUserRepositoryTest {
    @Autowired
    FirstUserRepository firstUserRepository;

    @Test
    void testInsert() {
        FirstUser admin = FirstUser.builder()
                .id(SFID.randomSFID())
                .nickname("admin")
                .username("admin")
                .password(PasswordHelper.getRandomPassword())
                .build();
        firstUserRepository.save(admin);

        FirstUser guest = FirstUser.builder()
                .id(SFID.randomSFID())
                .nickname("guest")
                .username("guest")
                .password(PasswordHelper.getRandomPassword())
                .build();
        firstUserRepository.save(guest);
    }

    @Test
    void testUpdate() {
        FirstUser user = firstUserRepository.findOne(Example.of(FirstUser.builder().username("admin").build())).orElseGet(FirstUser::new);
        user.setNickname("超级管理员1");
        user.setPassword(PasswordHelper.getRandomPassword());
        firstUserRepository.save(user);
    }

    @Test
    void testDeleteById() {
        FirstUser user = firstUserRepository.findOne(Example.of(FirstUser.builder().username("admin").build())).orElseGet(FirstUser::new);
        firstUserRepository.deleteById(user.getId());
    }

    @Test
    void testDeleteAll() {
        firstUserRepository.deleteAll();
    }

    @Test
    void testFindOne() {
        FirstUser user = firstUserRepository.findOne(Example.of(FirstUser.builder().username("admin").build())).orElseGet(FirstUser::new);
        log.info("{}", user);
    }

    @Test
    void testFindAll() {
        List<FirstUser> users = firstUserRepository.findAll();
        for (FirstUser user : users) {
            log.info("{}", user);
        }
    }
}
