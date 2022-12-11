package cn.maiaimei.demo.tx.service;

import cn.maiaimei.demo.tx.model.User;
import cn.maiaimei.demo.tx.repository.UserRepository;
import cn.maiaimei.framework.util.SFID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public void insert() {
        String name = UUID.randomUUID().toString().substring(0, 5);
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .id(SFID.nextId())
                .nickname(name)
                .username(name)
                .password(name)
                .isEnabled(Boolean.TRUE)
                .isDeleted(Boolean.FALSE)
                .gmtCreate(now)
                .gmtModified(now)
                .build();
        int affectedRowCount = userRepository.insert(user);
        log.info("受影响行：{}", affectedRowCount);
        // mock exception
        int res = 1 / 0;
    }
}
