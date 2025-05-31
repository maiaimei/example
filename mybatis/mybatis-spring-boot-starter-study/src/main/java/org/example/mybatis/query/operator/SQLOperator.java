package org.example.mybatis.query.operator;

import lombok.Getter;

@Getter
public enum SQLOperator {
  EQ("=", "%s = #{queryable.conditions[%d].value}"),

  NE("<>", "%s <> #{queryable.conditions[%d].value}"),

  GT(">", "%s > #{queryable.conditions[%d].value}"),

  GE(">=", "%s >= #{queryable.conditions[%d].value}"),

  LT("<", "%s < #{queryable.conditions[%d].value}"),

  LE("<=", "%s <= #{queryable.conditions[%d].value}"),

  LIKE("LIKE", "%s LIKE CONCAT('%%', #{queryable.conditions[%d].value}, '%%')"),

  NOT_LIKE("NOT LIKE", "%s NOT LIKE CONCAT('%%', #{queryable.conditions[%d].value}, '%%')"),

  IN("IN", """
      %s IN\s
      <foreach collection='queryable.conditions[%d].value' item='item' open='(' separator=',' close=')'>
      #{item}
      </foreach>"""),

  NOT_IN("NOT IN", """
      %s NOT IN\s
      <foreach collection='queryable.conditions[%d].value' item='item' open='(' separator=',' close=')'>
      #{item}
      </foreach>"""),

  BETWEEN("BETWEEN", "%s BETWEEN #{queryable.conditions[%d].value} AND #{queryable.conditions[%d].secondValue}"),

  IS_NULL("IS NULL", "%s IS NULL"),

  IS_NOT_NULL("IS NOT NULL", "%s IS NOT NULL");

  private final String operator;

  private final String format;

  SQLOperator(String operator, String format) {
    this.operator = operator;
    this.format = format;
  }

}

