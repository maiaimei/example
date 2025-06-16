package org.example.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.example.model.*;
import org.example.model.domain.ConcreteDomain;
import org.example.model.domain.ConcreteElementAList;
import org.example.model.domain.ConcreteElementBList;
import org.example.model.request.ConcreteDomainCreateOrUpdateRequest;
import org.example.mybatis.query.filter.Condition;
import org.example.mybatis.query.filter.QueryConditionBuilder;
import org.example.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
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

  public ConcreteDomain create(ConcreteDomainCreateOrUpdateRequest concreteDomainCreateOrUpdateRequest) {
    final ConcreteDomain concreteDomain = new ConcreteDomain();

    // 使用 Spring 的 BeanUtils 复制同名、同类型属性
    BeanUtils.copyProperties(concreteDomainCreateOrUpdateRequest, concreteDomain);

    // 设置额外的属性
    concreteDomain.setId(IdGenerator.nextId());
    concreteDomain.setConcreteElementAList(
        ConcreteElementAList.of(concreteDomainCreateOrUpdateRequest.getConcreteElementAList(), ConcreteElementAList.class));
    concreteDomain.setConcreteElementBList(
        ConcreteElementBList.of(concreteDomainCreateOrUpdateRequest.getConcreteElementBList(), ConcreteElementBList.class));
    concreteDomain.setCreatedAt(LocalDateTime.now());
    concreteDomain.setUpdatedAt(LocalDateTime.now());

    domainService.create(concreteDomain);
    return concreteDomain;
  }

  public ConcreteDomain update(ConcreteDomainCreateOrUpdateRequest concreteDomainCreateOrUpdateRequest) {
    final ConcreteDomain concreteDomain = new ConcreteDomain();

    // 使用 Spring 的 BeanUtils 复制同名、同类型属性
    BeanUtils.copyProperties(concreteDomainCreateOrUpdateRequest, concreteDomain);

    // 设置额外的属性
    concreteDomain.setConcreteElementAList(
        ConcreteElementAList.of(concreteDomainCreateOrUpdateRequest.getConcreteElementAList(), ConcreteElementAList.class));
    concreteDomain.setConcreteElementBList(
        ConcreteElementBList.of(concreteDomainCreateOrUpdateRequest.getConcreteElementBList(), ConcreteElementBList.class));
    concreteDomain.setUpdatedAt(LocalDateTime.now());

    domainService.update(concreteDomain);
    return concreteDomain;
  }

  public void deleteById(BigDecimal id) {
    domainService.deleteById(id);
  }
}
