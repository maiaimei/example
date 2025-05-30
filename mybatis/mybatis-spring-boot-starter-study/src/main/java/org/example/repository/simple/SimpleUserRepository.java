package org.example.repository.simple;

import org.apache.ibatis.annotations.Mapper;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SimpleUserRepository extends UserRepository {

}
