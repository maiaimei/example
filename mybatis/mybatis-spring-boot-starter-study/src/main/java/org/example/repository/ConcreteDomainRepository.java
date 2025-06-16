package org.example.repository;

import org.apache.ibatis.annotations.Mapper;
import org.example.model.domain.ConcreteDomain;
import org.example.mybatis.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ConcreteDomainRepository extends BaseRepository<ConcreteDomain> {

}
