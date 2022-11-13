package cn.maiaimei.example.multiple.jpa.ds2.repository;

import cn.maiaimei.example.multiple.jpa.ds2.entity.SecondUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecondUserRepository extends JpaRepository<SecondUser, Long> {
}
