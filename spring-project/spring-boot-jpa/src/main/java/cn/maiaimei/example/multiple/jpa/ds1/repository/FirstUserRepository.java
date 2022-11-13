package cn.maiaimei.example.multiple.jpa.ds1.repository;

import cn.maiaimei.example.multiple.jpa.ds1.entity.FirstUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FirstUserRepository extends JpaRepository<FirstUser, Long> {
}
