package org.example.repository.advanced;

import org.apache.ibatis.annotations.Mapper;
import org.example.model.domain.User;
import org.example.repository.BasicRepository;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AdvancedUserRepository extends BasicRepository<User> {

}
