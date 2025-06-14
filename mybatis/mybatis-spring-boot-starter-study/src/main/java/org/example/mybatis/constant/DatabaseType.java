package org.example.mybatis.constant;

import lombok.Getter;

@Getter
public enum DatabaseType {
  MYSQL("mysql"),

  ORACLE("oracle"),

  POSTGRESQL("postgresql");

  private final String type;

  DatabaseType(String type) {
    this.type = type;
  }
}