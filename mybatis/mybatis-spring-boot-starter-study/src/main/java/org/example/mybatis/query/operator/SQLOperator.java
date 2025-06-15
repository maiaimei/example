package org.example.mybatis.query.operator;

/**
 * SQL Operator Enum
 */
public enum SQLOperator {
  // ============================== Standard SQL Operators ==============================
  /**
   * {@code =} Equal to
   */
  EQ,          // 等于

  /**
   * {@code !=} or {@code <>} Not equal to
   */
  NE,          // 不等于

  /**
   * {@code >} Greater than
   */
  GT,          // 大于

  /**
   * {@code >=} Greater than or equal to
   */
  GE,          // 大于等于

  /**
   * {@code <} Less than
   */
  LT,          // 小于

  /**
   * {@code <=} Less than or equal to
   */
  LE,          // 小于等于

  /**
   * {@code LIKE} Pattern matching
   */
  LIKE,        // 模糊查询

  /**
   * {@code NOT LIKE} Pattern not matching
   */
  NOT_LIKE,    // NOT LIKE查询

  /**
   * {@code LIKE} Left-side wildcard matching
   */
  STARTS_WITH, // 左模糊

  /**
   * {@code LIKE} Right-side wildcard matching
   */
  ENDS_WITH,   // 右模糊

  /**
   * {@code BETWEEN} Range query
   */
  BETWEEN,     // 范围查询

  /**
   * {@code IN} Value in a list
   */
  IN,          // IN查询

  /**
   * {@code NOT IN} Value not in a list
   */
  NOT_IN,      // NOT IN查询

  /**
   * {@code IS NULL} Check if value is null
   */
  IS_NULL,     // 为空

  /**
   * {@code IS NOT NULL} Check if value is not null
   */
  IS_NOT_NULL, // 不为空

  // ============================== PostgreSQL Specific Operators ==============================
  // String Operators
  /**
   * {@code ILIKE} Case-insensitive LIKE
   */
  LIKE_CASE_INSENSITIVE,       // 大小写不敏感的 LIKE

  /**
   * {@code NOT ILIKE} Case-insensitive NOT LIKE
   */
  NOT_LIKE_CASE_INSENSITIVE,   // 大小写不敏感的 NOT LIKE

  // JSON Operators
  JSONB_TEXT_EQUALS,

  JSONB_TEXT_LIKE,

  JSONB_TEXT_NOT_LIKE,

  JSONB_ARRAY_CONTAINS,

  JSONB_ARRAY_LIKE,

  JSONB_OBJECT_ARRAY_EQUALS,

  JSONB_OBJECT_ARRAY_LIKE,

  // Array Operators
  /**
   * {@code @>} Array contains
   */
  ARRAY_CONTAINS // 数组包含
}