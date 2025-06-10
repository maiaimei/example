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

  public Product getProductById(BigDecimal id) {
    final Product product = new Product();
    product.setId(id);
    return productRepositoryService.select(product);
  }

  public void createProduct(Product product) {
    product.setId(IdGenerator.nextId());
    product.setCreateTime(LocalDateTime.now());
    productRepositoryService.create(product);
  }

  public void updataProduct(Product product) {
    productRepositoryService.update(product);
  }

  public void deleteProductById(BigDecimal id) {
    final Product product = new Product();
    product.setId(id);
    productRepositoryService.delete(product);
  }
}
