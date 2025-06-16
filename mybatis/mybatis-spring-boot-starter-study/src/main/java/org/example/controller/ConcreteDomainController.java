package org.example.controller;

import java.math.BigDecimal;
import org.example.model.ApiRequest;
import org.example.model.PageableSearchRequest;
import org.example.model.PageableSearchResult;
import org.example.model.domain.ConcreteDomain;
import org.example.service.ConcreteDomainBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/concrete-domains")
public class ConcreteDomainController {

  @Autowired
  ConcreteDomainBusinessService domainBusinessService;

  @PostMapping("/list")
  public PageableSearchResult<ConcreteDomain> getList(@RequestBody ApiRequest<PageableSearchRequest<ConcreteDomain>> request) {
    return domainBusinessService.getList(request.getData());
  }

  @GetMapping("/{id}")
  public ConcreteDomain getById(@PathVariable BigDecimal id) {
    return domainBusinessService.getById(id);
  }

  @PostMapping
  public ConcreteDomain create(@RequestBody ApiRequest<ConcreteDomain> request) {
    return domainBusinessService.create(request.getData());
  }

  @PutMapping
  public ConcreteDomain update(@RequestBody ApiRequest<ConcreteDomain> request) {
    return domainBusinessService.update(request.getData());
  }

  @DeleteMapping("/{id}")
  public void deleteById(@PathVariable BigDecimal id) {
    domainBusinessService.deleteById(id);
  }
}
