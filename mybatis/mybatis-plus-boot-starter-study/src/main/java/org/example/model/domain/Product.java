package org.example.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;

@Data
@TableName("PRODUCT_TEST")
public class Product {

  /**
   * 主键ID
   */
  @TableId(type = IdType.INPUT)
  private BigDecimal id;

  /**
   * 产品名称
   */
  private String productName;

//  /**
//   * 价格
//   */
//  private BigDecimal price;
//
//  /**
//   * 库存数量
//   */
//  private Integer stockQuantity;
//
//  /**
//   * 描述
//   */
//  private String description;
//
//  /**
//   * 标签
//   */
//  private String tags;
//
//  /**
//   * 状态
//   */
//  private String status;
//
//  /**
//   * 创建时间
//   */
//  private LocalDateTime createTime;
//
//  /**
//   * 属性
//   */
//  private String properties;
//
//  /**
//   * 搜索向量
//   */
//  private String searchVector;
//
//  /**
//   * 是否激活
//   */
//  private Boolean isActive;
//
//  /**
//   * 分类
//   */
//  @TableField(typeHandler = JacksonTypeHandler.class)
//  private List<String> categories; // 使用 JSON 存储
}