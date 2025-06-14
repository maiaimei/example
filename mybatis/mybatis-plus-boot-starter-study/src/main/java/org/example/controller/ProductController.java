package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.math.BigDecimal;
import org.example.model.ApiRequest;
import org.example.model.PageableSearchRequest;
import org.example.model.domain.Product;
import org.example.model.request.ProductFilterCriteria;
import org.example.service.ProductService;
import org.example.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

  @Autowired
  private ProductService productService;

  // 分页查询产品
  @PostMapping("/list")
  public Page<Product> list(@RequestBody ApiRequest<PageableSearchRequest<ProductFilterCriteria>> request) {
    final PageableSearchRequest<ProductFilterCriteria> searchRequest = request.getData();
    final ProductFilterCriteria filter = searchRequest.getFilter();
    Page<Product> page = new Page<>(searchRequest.getPage().getCurrent(), searchRequest.getPage().getSize());
    LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.like(StringUtils.hasText(filter.getProductName()), Product::getProductName, filter.getProductName());
    return productService.page(page, queryWrapper);
  }

  // 根据ID查询产品
  @GetMapping("/{id}")
  public Product getById(@PathVariable BigDecimal id) {
    return productService.getById(id);
  }

  // 创建产品
  @PostMapping
  public Product create(@RequestBody Product product) {
    product.setId(IdGenerator.nextId());
    productService.save(product);
    return product;
  }

  // 更新产品
  @PutMapping
  public boolean update(@RequestBody Product product) {
    return productService.updateById(product);
  }

  // 删除产品
  @DeleteMapping("/{id}")
  public boolean delete(@PathVariable BigDecimal id) {
    return productService.removeById(id);
  }

}
