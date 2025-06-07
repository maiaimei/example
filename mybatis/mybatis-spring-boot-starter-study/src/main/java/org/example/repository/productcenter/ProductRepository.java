package org.example.repository.productcenter;

import org.apache.ibatis.annotations.Mapper;
import org.example.model.domain.Product;
import org.example.repository.BasicRepository;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ProductRepository extends BasicRepository<Product> {

}
