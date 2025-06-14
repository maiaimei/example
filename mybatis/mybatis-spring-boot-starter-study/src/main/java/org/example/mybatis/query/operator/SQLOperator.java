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
   * {@code IN} Value in a list
   */
  IN,          // IN查询

  /**
   * {@code NOT IN} Value not in a list
   */
  NOT_IN,      // NOT IN查询

  /**
   * {@code BETWEEN} Range query
   */
  BETWEEN,     // 范围查询

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

  /**
   * {@code SIMILAR TO} Regex-like pattern matching
   */
  SIMILAR_TO,  // 正则匹配

  /**
   * {@code NOT SIMILAR TO} Regex-like pattern not matching
   */
  NOT_SIMILAR_TO, // 正则不匹配

  /**
   * {@code ~} Regex Match
   */
  REGEX_MATCH, // 正则匹配

  /**
   * {@code ~*} Case-insensitive Regex Match
   */
  REGEX_MATCH_CASE_INSENSITIVE, // 大小写不敏感的正则匹配

  /**
   * {@code !~} Regex Not Match
   */
  REGEX_NOT_MATCH, // 正则不匹配

  /**
   * {@code !~*} Case-insensitive Regex Not Match
   */
  REGEX_NOT_MATCH_CASE_INSENSITIVE, // 大小写不敏感的正则不匹配

  // JSON Operators
  JSONB_TEXT_EQUALS,

  JSONB_TEXT_LIKE,

  JSONB_TEXT_NOT_LIKE,

  JSONB_PATH_EXISTS,

  JSONB_PATH_EXISTS_ANY,

  JSONB_PATH_EXISTS_ALL,

  JSONB_ARRAY_CONTAINS,

  JSONB_ARRAY_LIKE,

  JSONB_OBJECT_ARRAY_EQUALS,

  JSONB_OBJECT_ARRAY_LIKE,

  /**
   * {@code @>} JSON contains
   */
  JSON_CONTAINS,  // JSON 包含操作

  /**
   * {@code <@} JSON contained by
   */
  JSON_CONTAINED_BY, // JSON 被包含操作

  // Array Operators
  /**
   * {@code =} Array equality
   */
  ARRAY_EQUALS, // 数组相等

  /**
   * {@code <>} Array inequality
   */
  ARRAY_NOT_EQUALS, // 数组不相等

  /**
   * {@code @>} Array contains
   */
  ARRAY_CONTAINS, // 数组包含

  /**
   * {@code <@} Array contained by
   */
  ARRAY_CONTAINED_BY, // 数组被包含

  /**
   * {@code &&} Array overlap
   */
  ARRAY_OVERLAP, // 数组重叠

  // Range Operators
  /**
   * {@code @>} Range contains element or range
   */
  RANGE_CONTAINS, // 范围包含元素或范围

  /**
   * {@code <@} Range contained by another range
   */
  RANGE_CONTAINED_BY, // 范围被另一个范围包含

  /**
   * {@code &&} Range overlap
   */
  RANGE_OVERLAP, // 范围重叠

  /**
   * {@code <<} Range strictly left of another range
   */
  RANGE_LEFT, // 范围严格在另一个范围左侧

  /**
   * {@code >>} Range strictly right of another range
   */
  RANGE_RIGHT, // 范围严格在另一个范围右侧

  /**
   * {@code -|-} Range adjacent to another range
   */
  RANGE_ADJACENT, // 范围与另一个范围相邻

  /**
   * {@code IS DISTINCT FROM} Check if values are distinct
   */
  IS_DISTINCT_FROM, // 判断值是否不同

  /**
   * {@code IS NOT DISTINCT FROM} Check if values are not distinct
   */
  IS_NOT_DISTINCT_FROM // 判断值是否不不同
}