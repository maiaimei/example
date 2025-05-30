package org.example.repository.master;

import org.apache.ibatis.annotations.Mapper;
import org.example.repository.BasicUserRepository;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MasterUserRepository extends BasicUserRepository {

}
