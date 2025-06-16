package org.example.service;

import org.example.model.domain.ConcreteDomain;
import org.example.mybatis.service.AbstractBaseService;
import org.example.repository.ConcreteDomainRepository;
import org.springframework.stereotype.Service;

@Service
public class ConcreteDomainService extends AbstractBaseService<ConcreteDomain, ConcreteDomainRepository> {

  public ConcreteDomainService(ConcreteDomainRepository repository) {
    super(repository);
  }
}
