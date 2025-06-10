package org.example.service.productcenter;

import java.math.BigDecimal;
import org.example.datasource.DataSource;
import org.example.model.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@DataSource("productCenter")
public class ProductBusinessService {

  @Autowired
  private ProductService productService;

  public Product getProductById(BigDecimal id) {
    return productService.getProductById(id);
  }

  public void createProduct(Product product) {
    productService.create(product);
  }

  public void updateProduct(Product product) {
    productService.update(product);
  }

  public void deleteProductById(BigDecimal id) {
    productService.deleteProductById(id);
  }
}
