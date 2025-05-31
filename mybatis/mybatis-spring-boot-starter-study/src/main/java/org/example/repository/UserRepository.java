package org.example.repository;

import org.apache.ibatis.annotations.Mapper;
import org.example.model.domain.User;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserRepository extends BasicRepository<User> {

}
