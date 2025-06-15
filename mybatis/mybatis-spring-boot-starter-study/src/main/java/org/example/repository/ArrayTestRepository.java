package org.example.repository;

import org.apache.ibatis.annotations.Mapper;
import org.example.model.domain.ArrayTest;
import org.example.mybatis.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ArrayTestRepository extends BaseRepository<ArrayTest> {

}
