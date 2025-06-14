package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.math.BigDecimal;
import org.example.model.ApiRequest;
import org.example.model.PageableSearchRequest;
import org.example.model.domain.Example;
import org.example.model.request.ExampleFilterCriteria;
import org.example.service.ExampleService;
import org.example.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/examples")
public class ExampleController {

  @Autowired
  private ExampleService exampleService;

  // 分页查询产品
  @PostMapping("/list")
  public Page<Example> list(@RequestBody ApiRequest<PageableSearchRequest<ExampleFilterCriteria>> request) {
    final PageableSearchRequest<ExampleFilterCriteria> searchRequest = request.getData();
    final ExampleFilterCriteria filter = searchRequest.getFilter();
    Page<Example> page = new Page<>(searchRequest.getPage().getCurrent(), searchRequest.getPage().getSize());
    LambdaQueryWrapper<Example> queryWrapper = new LambdaQueryWrapper<>();
    return exampleService.page(page, queryWrapper);
  }

  // 根据ID查询
  @GetMapping("/{id}")
  public Example getById(@PathVariable BigDecimal id) {
    return exampleService.getById(id);
  }

  // 创建
  @PostMapping
  public Example create(@RequestBody Example example) {
    example.setId(IdGenerator.nextId());
    exampleService.save(example);
    return example;
  }

  // 更新
  @PutMapping
  public boolean update(@RequestBody Example example) {
    return exampleService.updateById(example);
  }

  // 删除
  @DeleteMapping("/{id}")
  public boolean delete(@PathVariable BigDecimal id) {
    return exampleService.removeById(id);
  }

}
