package org.example.service.productcenter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.example.datasource.DataSource;
import org.example.model.domain.Product;
import org.example.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@DataSource("productCenter")
public class ProductService {

  @Autowired
  private ProductRepositoryService productRepositoryService;

  public void create(Product product) {
    product.setId(IdGenerator.nextId());
    product.setCreateTime(LocalDateTime.now());
    productRepositoryService.create(product);
  }

  public void update(Product product) {
    productRepositoryService.update(product);
  }

  public void deleteById(BigDecimal id) {
    final Product product = new Product();
    product.setId(id);
    productRepositoryService.delete(product);
  }

  public Product selectById(BigDecimal id) {
    final Product product = new Product();
    product.setId(id);
    return productRepositoryService.select(product);
  }
}
