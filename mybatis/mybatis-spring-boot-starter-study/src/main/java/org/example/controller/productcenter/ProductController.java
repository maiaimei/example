package org.example.controller.productcenter;

import java.math.BigDecimal;
import java.util.List;
import org.example.model.domain.Product;
import org.example.service.productcenter.ProductService;
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
@RequestMapping("/products")
public class ProductController {

  @Autowired
  private ProductService productService;

  @PostMapping("/list")
  public List<Product> listProducts() {
    return null;
  }

  @GetMapping("/{id}")
  public Product getProductById(@PathVariable BigDecimal id) {
    return productService.selectById(id);
  }

  @PostMapping
  public Product createProduct(@RequestBody Product product) {
    productService.create(product);
    return product;
  }

  @PutMapping
  public Product updataProduct(@RequestBody Product product) {
    productService.update(product);
    return product;
  }

  @DeleteMapping("/{id}")
  public void deleteProductById(@PathVariable BigDecimal id) {
    productService.deleteById(id);
  }
}
