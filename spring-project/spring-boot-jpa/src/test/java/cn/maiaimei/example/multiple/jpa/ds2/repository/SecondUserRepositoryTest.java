package cn.maiaimei.example.multiple.jpa.ds2.repository;

import cn.maiaimei.example.multiple.jpa.ds2.entity.SecondUser;
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
public class SecondUserRepositoryTest {
    @Autowired
    SecondUserRepository firstUserRepository;

    @Test
    void testInsert() {
        LocalDateTime now = LocalDateTime.now();

        SecondUser admin = SecondUser.builder()
                .id(SFID.randomSFID())
                .nickname("admin")
                .username("admin")
                .password(PasswordHelper.getRandomPassword())
                .is_enabled(Boolean.TRUE)
                .is_deleted(Boolean.FALSE)
                .gmtCreate(now)
                .gmtModified(now)
                .build();
        firstUserRepository.save(admin);

        SecondUser guest = SecondUser.builder()
                .id(SFID.randomSFID())
                .nickname("guest")
                .username("guest")
                .password(PasswordHelper.getRandomPassword())
                .is_enabled(Boolean.TRUE)
                .is_deleted(Boolean.FALSE)
                .gmtCreate(now)
                .gmtModified(now)
                .build();
        firstUserRepository.save(guest);
    }

    @Test
    void testUpdate() {
        LocalDateTime now = LocalDateTime.now();
        SecondUser user = firstUserRepository.findOne(Example.of(SecondUser.builder().username("admin").build())).orElseGet(SecondUser::new);
        user.setIs_enabled(Boolean.FALSE);
        user.setGmtModified(LocalDateTime.now());
        firstUserRepository.save(user);
    }

    @Test
    void testDeleteById() {
        SecondUser user = firstUserRepository.findOne(Example.of(SecondUser.builder().username("admin").build())).orElseGet(SecondUser::new);
        firstUserRepository.deleteById(user.getId());
    }

    @Test
    void testDeleteAll() {
        firstUserRepository.deleteAll();
    }

    @Test
    void testFindOne() {
        SecondUser user = firstUserRepository.findOne(Example.of(SecondUser.builder().username("admin").build())).orElseGet(SecondUser::new);
        log.info("{}", user);
    }

    @Test
    void testFindAll() {
        List<SecondUser> users = firstUserRepository.findAll();
        for (SecondUser user : users) {
            log.info("{}", user);
        }
    }
}
