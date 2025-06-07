package org.example.service.productcenter;

import org.example.model.domain.Product;
import org.example.repository.productcenter.ProductRepository;
import org.example.service.AbstractDomainRepositoryService;
import org.springframework.stereotype.Service;

@Service
public class ProductRepositoryService extends AbstractDomainRepositoryService<Product, ProductRepository> {

  public ProductRepositoryService(ProductRepository repository) {
    super(repository);
  }
}
