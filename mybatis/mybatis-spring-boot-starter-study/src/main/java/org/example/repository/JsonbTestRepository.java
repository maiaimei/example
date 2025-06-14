package org.example.repository;

import org.apache.ibatis.annotations.Mapper;
import org.example.model.domain.JsonbTest;
import org.example.mybatis.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface JsonbTestRepository extends BaseRepository<JsonbTest> {

}
