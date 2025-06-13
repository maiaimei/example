package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.model.domain.Product;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

}
