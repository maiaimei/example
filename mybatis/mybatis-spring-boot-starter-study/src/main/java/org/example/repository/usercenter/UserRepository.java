package org.example.repository.usercenter;

import org.apache.ibatis.annotations.Mapper;
import org.example.model.domain.User;
import org.example.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserRepository extends BaseRepository<User> {

}
