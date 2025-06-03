package org.example.datasource;

import lombok.Getter;

@Getter
public enum DatabaseType {

  ORACLE("oracle"),

  MYSQL("mysql"),

  POSTGRESQL("postgresql");

  private final String type;

  DatabaseType(String type) {
    this.type = type;
  }
}
