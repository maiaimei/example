package org.example.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.example.model.*;
import org.example.model.domain.ConcreteDomain;
import org.example.mybatis.query.filter.Condition;
import org.example.mybatis.query.filter.QueryConditionBuilder;
import org.example.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConcreteDomainBusinessService {

  @Autowired
  private ConcreteDomainService domainService;

  public PageableSearchResult<ConcreteDomain> getList(PageableSearchRequest<ConcreteDomain> request) {
    final ConcreteDomain filter = request.getFilter();
    final List<SortCriteria> sort = request.getSort();
    final PageCriteria page = request.getPage();

    final ConcreteDomain concreteDomain = new ConcreteDomain();
    final List<Condition> conditions = QueryConditionBuilder.create().build();
    return domainService.advancedSelect(
        concreteDomain, conditions, null, sort, page, domain -> domain);
  }

  public ConcreteDomain getById(BigDecimal id) {
    return domainService.selectById(id);
  }

  public ConcreteDomain create(ConcreteDomain concreteDomain) {
    concreteDomain.setId(IdGenerator.nextId());
    concreteDomain.setCreatedAt(LocalDateTime.now());
    concreteDomain.setUpdatedAt(LocalDateTime.now());
    domainService.create(concreteDomain);
    return concreteDomain;
  }

  public ConcreteDomain update(ConcreteDomain concreteDomain) {
    concreteDomain.setUpdatedAt(LocalDateTime.now());
    domainService.update(concreteDomain);
    return concreteDomain;
  }

  public void deleteById(BigDecimal id) {
    domainService.deleteById(id);
  }
}
