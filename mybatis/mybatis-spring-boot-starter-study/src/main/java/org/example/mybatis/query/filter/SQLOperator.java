package org.example.mybatis.query.filter;

public enum SQLOperator {
  EQ("="),
  NE("<>"),
  GT(">"),
  GE(">="),
  LT("<"),
  LE("<="),
  LIKE("LIKE"),
  NOT_LIKE("NOT LIKE"),
  IN("IN"),
  NOT_IN("NOT IN"),
  BETWEEN("BETWEEN"),
  IS_NULL("IS NULL"),
  IS_NOT_NULL("IS NOT NULL");

  private final String operator;

  SQLOperator(String operator) {
    this.operator = operator;
  }

  public String getOperator() {
    return operator;
  }
}

