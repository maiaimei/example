package org.example.repository.slave1;

import org.apache.ibatis.annotations.Mapper;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface Slave1UserRepository extends UserRepository {

}
