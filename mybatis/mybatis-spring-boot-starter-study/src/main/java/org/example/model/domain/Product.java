package org.example.model.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import org.example.mybatis.annotation.TableName;

@Data
@TableName("\"PRODUCT_TEST\"")
public class Product {

  /**
   * 产品编号
   */
  private BigDecimal id;

  /**
   * 产品名称
   */
  private String productName;

  /**
   * 产品描述
   */
  private String description;

  /**
   * 产品分类
   * 数据类型: ARRAY, VARCHAR[]
   * 示例值: ARRAY['electronics', 'computers']
   */
  private List<String> categories;

  /**
   * 产品标签
   * 数据类型: JSONB
   * 示例值: ["laptop", "apple"]
   */
  private String tags;

  /**
   * 产品属性
   * 数据类型: JSONB
   * 示例值: {"color": "silver", "ram": "16GB"}
   */
  private String properties;


  /**
   * 搜索向量
   */
  private String searchVector;

  /**
   * 产品价格
   */
  private BigDecimal price;

  /**
   * 库存数量
   */
  private Integer stockQuantity;

  /**
   * 产品状态
   */
  private String status;

  /**
   * 是否激活
   */
  private Boolean isActive;

  /**
   * 创建时间
   */
  private LocalDateTime createTime;
}