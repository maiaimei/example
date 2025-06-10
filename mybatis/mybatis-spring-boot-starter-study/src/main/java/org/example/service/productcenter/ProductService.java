package org.example.service.productcenter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.example.model.domain.Product;
import org.example.repository.productcenter.ProductRepository;
import org.example.service.AbstractBaseService;
import org.example.utils.IdGenerator;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends AbstractBaseService<Product, ProductRepository> {

  public ProductService(ProductRepository repository) {
    super(repository);
  }

  public Product getProductById(BigDecimal id) {
    final Product product = new Product();
    product.setId(id);
    return select(product);
  }

  public void createProduct(Product product) {
    product.setId(IdGenerator.nextId());
    product.setCreateTime(LocalDateTime.now());
    product.setIsActive(Boolean.TRUE);
    create(product);
  }

  public void updateProduct(Product product) {
    update(product);
  }

  public void deleteProductById(BigDecimal id) {
    final Product product = new Product();
    product.setId(id);
    delete(product);
  }
}
