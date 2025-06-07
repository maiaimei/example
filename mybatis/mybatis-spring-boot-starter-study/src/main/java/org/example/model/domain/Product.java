package org.example.model.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.example.mybatis.annotation.TableName;

@Data
@TableName("PRODUCT_TEST")
public class Product {

  /**
   * 主键ID
   */
  private BigDecimal id;

  /**
   * 产品名称
   */
  private String productName;

  /**
   * 价格
   */
  private BigDecimal price;

  /**
   * 库存数量
   */
  private Integer stockQuantity;

  /**
   * 描述
   */
  private String description;

  /**
   * 标签
   */
  private List<String> tags;

  /**
   * 状态
   */
  private String status;

  /**
   * 创建时间
   */
  private LocalDateTime createTime;

  /**
   * 属性
   */
  private Map<String, Object> properties;

  /**
   * 搜索向量
   */
  private String searchVector;

  /**
   * 是否激活
   */
  private Boolean isActive;
}

