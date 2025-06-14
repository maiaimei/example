package org.example.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("\"PRODUCT_TEST\"")
public class Product {

  /**
   * 产品编号
   */
  @TableId(value = "\"ID\"", type = IdType.INPUT)
  private BigDecimal id;

  /**
   * 产品名称
   */
  @TableField(value = "\"PRODUCT_NAME\"")
  private String productName;

  /**
   * 产品描述
   */
  @TableField(value = "\"DESCRIPTION\"")
  private String description;

  /**
   * 产品分类
   * 使用 JacksonTypeHandler 处理 JSON 数组
   * 数据类型: ARRAY, VARCHAR[]
   * 示例值: ARRAY['electronics', 'computers']
   */
  @TableField(value = "\"CATEGORIES\"")
  private String categories;

  /**
   * 产品标签
   * 数据类型: JSONB
   * 示例值: ["laptop", "apple"]
   */
  @TableField(value = "\"TAGS\"")
  private String tags;

  /**
   * 产品属性
   * 使用 JacksonTypeHandler 处理 JSON 对象
   * 数据类型: JSONB
   * 示例值: {"color": "silver", "ram": "16GB"}
   */
  @TableField(value = "\"PROPERTIES\"")
  private String properties;


  /**
   * 搜索向量
   */
  @TableField(value = "\"SEARCH_VECTOR\"")
  private String searchVector;

  /**
   * 产品价格
   */
  @TableField(value = "\"PRICE\"")
  private BigDecimal price;

  /**
   * 库存数量
   */
  @TableField(value = "\"STOCK_QUANTITY\"")
  private Integer stockQuantity;

  /**
   * 产品状态
   */
  @TableField(value = "\"STATUS\"")
  private String status;

  /**
   * 是否激活
   */
  @TableField(value = "\"IS_ACTIVE\"")
  private Boolean isActive;

  /**
   * 创建时间
   */
  @TableField(value = "\"CREATE_TIME\"")
  private LocalDateTime createTime;
}